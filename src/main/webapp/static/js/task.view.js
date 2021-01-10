$(function() {

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
