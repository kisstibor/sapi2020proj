$(function() {

    $(".well").on("click", "#delete-story-link", function(e) {
        e.preventDefault();

        var storyDeleteDialogTempate = Handlebars.compile($("#template-delete-story-confirmation-dialog").html());

        $("#view-holder").append(storyDeleteDialogTempate());
        $("#delete-story-confirmation-dialog").modal();
    })

    $("#view-holder").on("click", "#cancel-story-button", function(e) {
        e.preventDefault();

        var deleteConfirmationDialog = $("#delete-story-confirmation-dialog")
        deleteConfirmationDialog.modal('hide');
        deleteConfirmationDialog.remove();
    });

    $("#view-holder").on("click", "#delete-story-button", function(e) {
        e.preventDefault();
        window.location.href = "/story/delete/" + $("#story-id").text();
    });
    
    
    
    
    $(".well").on("click", "#delete-task-link", function(e) {
        e.preventDefault();

        var taskDeleteDialogTempate = Handlebars.compile($("#template-delete-task-confirmation-dialog").html());

        $("#view-holder").append(taskDeleteDialogTempate());
        $("#delete-task-confirmation-dialog").modal();
    })
    
    $("#view-holder").on("click", "#cancel-task-button", function(e) {
        e.preventDefault();

        var deleteConfirmationDialog = $("#delete-task-confirmation-dialog")
        deleteConfirmationDialog.modal('hide');
        deleteConfirmationDialog.remove();
    });

    $("#view-holder").on("click", "#delete-task-button", function(e) {
        e.preventDefault();
        window.location.href = "/task/delete/" + $("#task-id").text();
    });
});
