$(function() {

    $(".well").on("click", "#delete-worklog-link", function(e) {
        e.preventDefault();

        var worklogDeleteDialogTempate = Handlebars.compile($("#template-delete-worklog-confirmation-dialog").html());

        $("#view-holder").append(worklogDeleteDialogTempate());
        $("#delete-worklog-confirmation-dialog").modal();
    })

    $("#view-holder").on("click", "#cancel-worklog-button", function(e) {
        e.preventDefault();

        var deleteConfirmationDialog = $("#delete-worklog-confirmation-dialog")
        deleteConfirmationDialog.modal('hide');
        deleteConfirmationDialog.remove();
    });

    $("#view-holder").on("click", "#delete-worklog-button", function(e) {
        e.preventDefault();
        window.location.href = "/worklog/delete/" + $("#worklog-id").text();
    });
});
