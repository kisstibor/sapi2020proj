$(function() {

    $(".well").on("click", "#delete-log-link", function(e) {
        e.preventDefault();

        var logDeleteDialogTempate = Handlebars.compile($("#template-delete-log-confirmation-dialog").html());

        $("#view-holder").append(logDeleteDialogTempate());
        $("#delete-log-confirmation-dialog").modal();
    })

    $("#view-holder").on("click", "#cancel-log-button", function(e) {
        e.preventDefault();

        var deleteConfirmationDialog = $("#delete-log-confirmation-dialog")
        deleteConfirmationDialog.modal('hide');
        deleteConfirmationDialog.remove();
    });

    $("#view-holder").on("click", "#delete-log-button", function(e) {
        e.preventDefault();
        window.location.href = "/log/delete/" + $("#log-id").text();
    });
});
