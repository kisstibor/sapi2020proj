$(function() {

    $(".well").on("click", "#delete-bug-link", function(e) {
        e.preventDefault();

        var bugDeleteDialogTemplate = Handlebars.compile($("#template-delete-bug-confirmation-dialog").html());

        $("#view-holder").append(bugDeleteDialogTemplate());
        $("#delete-bug-confirmation-dialog").modal();
    })

    $("#view-holder").on("click", "#cancel-bug-button", function(e) {
        e.preventDefault();

        var deleteConfirmationDialog = $("#delete-bug-confirmation-dialog")
        deleteConfirmationDialog.modal('hide');
        deleteConfirmationDialog.remove();
    });

    $("#view-holder").on("click", "#delete-bug-button", function(e) {
        e.preventDefault();
        window.location.href = "/bug/delete/" + $("#bug-id").text();
    });
});
