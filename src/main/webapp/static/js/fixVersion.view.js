$(function() {

    $(".well").on("click", "#delete-fixVersion-link", function(e) {
        e.preventDefault();

        var storyDeleteDialogTempate = Handlebars.compile($("#template-delete-fixVersion-confirmation-dialog").html());

        $("#view-holder").append(storyDeleteDialogTempate());
        $("#delete-fixVersion-confirmation-dialog").modal();
    })

    $("#view-holder").on("click", "#cancel-fixVersion-button", function(e) {
        e.preventDefault();

        var deleteConfirmationDialog = $("#delete-fixVersion-confirmation-dialog")
        deleteConfirmationDialog.modal('hide');
        deleteConfirmationDialog.remove();
    });

    $("#view-holder").on("click", "#delete-fixVersion-button", function(e) {
        e.preventDefault();
        window.location.href = "/fixVersion/delete/" + $("#fixVersion-id").text();
    });
});
