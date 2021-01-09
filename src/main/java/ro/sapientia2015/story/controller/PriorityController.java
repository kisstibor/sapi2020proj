package ro.sapientia2015.story.controller;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ro.sapientia2015.story.dto.PriorityDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Priority;
import ro.sapientia2015.story.service.PriorityService;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.util.List;
import java.util.Locale;

/**
 * @author Kapas Krisztina
 */
@Controller
@SessionAttributes( "priority" )
public class PriorityController 
{

	protected static final String FEEDBACK_MESSAGE_KEY_ADDED = "Priority added";
    protected static final String FEEDBACK_MESSAGE_KEY_UPDATED	= "Priority updated";
    protected static final String FEEDBACK_MESSAGE_KEY_DELETED 	= "Priority deleted";

    protected static final String FLASH_MESSAGE_KEY_ERROR 		= "errorMessage";
    protected static final String FLASH_MESSAGE_KEY_FEEDBACK	= "feedbackMessage";

    protected static final String MODEL_ATTRIBUTE 		= "priority";
    protected static final String MODEL_ATTRIBUTE_LIST	= "priorities";

    protected static final String PARAMETER_ID = "id";

    protected static final String REQUEST_MAPPING_LIST = "/priority/list";
    protected static final String REQUEST_MAPPING_VIEW = "/priority/{id}";

    protected static final String VIEW_ADD 		= "priority/add";
    protected static final String VIEW_LIST 	= "priority/list";
    protected static final String VIEW_UPDATE	= "priority/update";
    protected static final String VIEW_VIEW 	= "priority/view";
    

    @Resource
    private PriorityService service;

    @Resource
    private MessageSource messageSource;

    @RequestMapping( value = "/priority/add", method = RequestMethod.GET )
    public String showAddForm( Model model ) 
    {
        PriorityDTO formObject	= new PriorityDTO();
        
        model.addAttribute( MODEL_ATTRIBUTE, formObject );

        return VIEW_ADD;
    }

    @RequestMapping( value = "/priority/add", method = RequestMethod.POST )
    public String add( @Valid @ModelAttribute( MODEL_ATTRIBUTE ) PriorityDTO dto, BindingResult result, RedirectAttributes attributes )
    {
        if ( result.hasErrors() ) 
        {
            return VIEW_ADD;
        }

        Priority added = service.add( dto );
        
        addFeedbackMessage( attributes, FEEDBACK_MESSAGE_KEY_ADDED, added.getName() );
        attributes.addAttribute( PARAMETER_ID, added.getId() );

        return createRedirectViewPath( REQUEST_MAPPING_VIEW );
    }

    @RequestMapping( value = "/priority/delete/{id}", method = RequestMethod.GET )
    public String deleteById( @PathVariable("id") Long id, RedirectAttributes attributes ) throws NotFoundException
    {
    	Priority deleted = service.deleteById( id );
    	
        addFeedbackMessage( attributes, FEEDBACK_MESSAGE_KEY_DELETED, deleted.getName() );
        return createRedirectViewPath( "/priority/list" );
    }

    @RequestMapping( value = VIEW_LIST, method = RequestMethod.GET )
    public String findAll( Model model ) 
    {
        List<Priority> models = service.findAll();
        
        model.addAttribute( MODEL_ATTRIBUTE_LIST, models );
        
        return VIEW_LIST;
    }

    @RequestMapping( value = REQUEST_MAPPING_VIEW, method = RequestMethod.GET )
    public String findById(@PathVariable("id") Long id, Model model) throws NotFoundException 
    {
        Priority found = service.findById( id );
        
        model.addAttribute( MODEL_ATTRIBUTE, found );
        
        return VIEW_VIEW;
    }

    @RequestMapping( value = "/priority/update/{id}", method = RequestMethod.GET )
    public String showUpdateForm( @PathVariable("id") Long id, Model model ) throws NotFoundException
    {
        Priority updated 		= service.findById( id );
        PriorityDTO formObject	= constructFormObjectForUpdateForm( updated );
        
        model.addAttribute( MODEL_ATTRIBUTE, formObject );

        return VIEW_UPDATE;
    }

    @RequestMapping( value = "/priority/update", method = RequestMethod.POST )
    public String update( @Valid @ModelAttribute( MODEL_ATTRIBUTE ) PriorityDTO dto, BindingResult result, RedirectAttributes attributes ) throws NotFoundException
    {
        if ( result.hasErrors() ) 
        {
            return VIEW_UPDATE;
        }

        Priority updated = service.update( dto );
        
        addFeedbackMessage( attributes, FEEDBACK_MESSAGE_KEY_UPDATED, updated.getName() );
        attributes.addAttribute( PARAMETER_ID, updated.getId() );

        return createRedirectViewPath( REQUEST_MAPPING_VIEW );
    }

    private PriorityDTO constructFormObjectForUpdateForm( Priority updated )
    {
    	PriorityDTO dto = new PriorityDTO();

        dto.setId( updated.getId() );
        dto.setName( updated.getName() );

        return dto;
    }

    private void addFeedbackMessage( RedirectAttributes attributes, String messageCode, Object... messageParameters ) 
    {
        String localizedFeedbackMessage = getMessage( messageCode, messageParameters );
        
        attributes.addFlashAttribute( FLASH_MESSAGE_KEY_FEEDBACK, localizedFeedbackMessage );
    }

    private String getMessage( String messageCode, Object... messageParameters ) 
    {
        Locale current = LocaleContextHolder.getLocale();
        
        return messageSource.getMessage( messageCode, messageParameters, current );
    }

   private String createRedirectViewPath( String requestMapping ) 
   {
        StringBuilder redirectViewPath = new StringBuilder();
        
        redirectViewPath.append( "redirect:" );
        redirectViewPath.append( requestMapping );
        
        return redirectViewPath.toString();
   }
}
