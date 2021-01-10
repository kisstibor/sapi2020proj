package ro.sapientia2015.story.controller;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ro.sapientia2015.story.dto.LabelDTO;
import ro.sapientia2015.story.model.Label;
import ro.sapientia2015.story.service.LabelService;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.util.List;
import java.util.Locale;


@Controller
@SessionAttributes("label")
public class LabelController {

    protected static final String FEEDBACK_MESSAGE_KEY_ADDED = "feedback.message.label.added";

    protected static final String FLASH_MESSAGE_KEY_ERROR = "errorMessage";
    protected static final String FLASH_MESSAGE_KEY_FEEDBACK = "feedbackMessage";

    protected static final String MODEL_ATTRIBUTE = "label";
    protected static final String MODEL_ATTRIBUTE_LIST = "labels";


    protected static final String VIEW_LIST = "label/list";
    protected static final String VIEW_ADD = "label/add";

    @Resource
    private LabelService service;

    @Resource
    private MessageSource messageSource;

    @RequestMapping(value = "/label", method = RequestMethod.GET)
    public String showAddForm(Model model) {

//		List<Label> labels = service.findAll();
//		model.addAttribute("labels", labels);

        return VIEW_LIST;
    }

//    @RequestMapping(value = "/label", method = RequestMethod.POST)
//    public String add(@Valid @ModelAttribute(MODEL_ATTRIBUTE) LabelDTO dto, BindingResult result, RedirectAttributes attributes) {
//        Label added = service.add(dto);
//        addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_ADDED, added.getTitle());
//
//        return VIEW_ADD;
//    }

    private void addFeedbackMessage(RedirectAttributes attributes, String messageCode, Object... messageParameters) {
        String localizedFeedbackMessage = getMessage(messageCode, messageParameters);
        attributes.addFlashAttribute(FLASH_MESSAGE_KEY_FEEDBACK, localizedFeedbackMessage);
    }

    private String getMessage(String messageCode, Object... messageParameters) {
        Locale current = LocaleContextHolder.getLocale();
        return messageSource.getMessage(messageCode, messageParameters, current);
    }
}
