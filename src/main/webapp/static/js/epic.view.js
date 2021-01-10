$(function() {

    $(".well").on("click", "#delete-epic-link", function(e) {
        e.preventDefault();

        var epicDeleteDialogTempate = Handlebars.compile($("#template-delete-epic-confirmation-dialog").html());

        $("#view-holder").append(epicDeleteDialogTempate());
        $("#delete-epic-confirmation-dialog").modal();
    })

    $("#view-holder").on("click", "#cancel-epic-button", function(e) {
        e.preventDefault();

        var deleteConfirmationDialog = $("#delete-epic-confirmation-dialog")
        deleteConfirmationDialog.modal('hide');
        deleteConfirmationDialog.remove();
    });

    $("#view-holder").on("click", "#delete-epic-button", function(e) {
        e.preventDefault();
        window.location.href = "/epic/delete/" + $("#epic-id").text();
    });
});
