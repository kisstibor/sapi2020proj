$(function() {

    $(".well").on("click", "#delete-goal-link", function(e) {
        e.preventDefault();

        var goalDeleteDialogTempate = Handlebars.compile($("#template-delete-goal-confirmation-dialog").html());

        $("#view-holder").append(goalDeleteDialogTempate());
        $("#delete-goal-confirmation-dialog").modal();
    })

    $("#view-holder").on("click", "#cancel-goal-button", function(e) {
        e.preventDefault();

        var deleteConfirmationDialog = $("#delete-goal-confirmation-dialog")
        deleteConfirmationDialog.modal('hide');
        deleteConfirmationDialog.remove();
    });

    $("#view-holder").on("click", "#delete-goal-button", function(e) {
        e.preventDefault();
        window.location.href = "/goal/delete/" + $("#goal-id").text();
    });
});