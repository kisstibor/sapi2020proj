$(function() {

    $(".well").on("click", "#delete-project-link", function(e) {
        e.preventDefault();

        var projectDeleteDialogTempate = Handlebars.compile($("#template-delete-project-confirmation-dialog").html());

        $("#view-holder").append(projectDeleteDialogTempate());
        $("#delete-project-confirmation-dialog").modal();
    })

    $("#view-holder").on("click", "#cancel-project-button", function(e) {
        e.preventDefault();

        var deleteConfirmationDialog = $("#delete-project-confirmation-dialog")
        deleteConfirmationDialog.modal('hide');
        deleteConfirmationDialog.remove();
    });

    $("#view-holder").on("click", "#delete-project-button", function(e) {
        e.preventDefault();
        window.location.href = "/project/delete/" + $("#project-id").text();
    });
});
