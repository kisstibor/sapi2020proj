$(function() {

    $(".well").on("click", "#delete-daily-link", function(e) {
        e.preventDefault();

        var storyDeleteDialogTempate = Handlebars.compile($("#template-delete-daily-confirmation-dialog").html());

        $("#view-holder").append(storyDeleteDialogTempate());
        $("#delete-daily-confirmation-dialog").modal();
    })

    $("#view-holder").on("click", "#cancel-daily-button", function(e) {
        e.preventDefault();

        var deleteConfirmationDialog = $("#delete-daily-confirmation-dialog")
        deleteConfirmationDialog.modal('hide');
        deleteConfirmationDialog.remove();
    });

    $("#view-holder").on("click", "#delete-daily-button", function(e) {
        e.preventDefault();
        window.location.href = "/daily/delete/" + $("#daily-id").text();
    });
});
