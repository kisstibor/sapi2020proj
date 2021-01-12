package ro.sapientia2015.story;

import org.springframework.test.util.ReflectionTestUtils;

import ro.sapientia2015.story.dto.GoalDTO;
import ro.sapientia2015.story.model.Goal;

public class GoalTestUtil {
	public static final Integer ID = 1;
	public static final String GOAL = "goal";
	public static final String GOAL_UPDATED = "updatedGoal";
	public static final String METHOD = "method";
	public static final String METHOD_UPDATED = "updatedMoethod";
	public static final String METRICS = "metrics";
	public static final String METRICS_UPDATED = "updatedMetrics";
	
    private static final String CHARACTER = "a";
    
    public static GoalDTO createFormObject(Integer id, String goal, String method, String metrics) {
    	GoalDTO dto = new GoalDTO();

        dto.setId(id);
        dto.setMethod(method);
        dto.setMetrics(metrics);

        return dto;
    }
    
    public static Goal createModel(Integer id, String goal, String method, String metrics) {
        Goal model = Goal
        		.getBuilder(goal)
        		.setGoal(goal)
                .setMethod(method)
                .setMetrics(metrics)
                .build();

        ReflectionTestUtils.setField(model, "id", id);

        return model;
    }
    
    public static String createRedirectViewPath(String path) {
        StringBuilder redirectViewPath = new StringBuilder();
        redirectViewPath.append("redirect:");
        redirectViewPath.append(path);
        return redirectViewPath.toString();
    }
    
    public static String createStringWithLength(int length) {
        StringBuilder builder = new StringBuilder();

        for (int index = 0; index < length; index++) {
            builder.append(CHARACTER);
        }

        return builder.toString();
    }

}
