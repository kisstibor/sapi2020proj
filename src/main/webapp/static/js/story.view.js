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




    $(".well").on("click", "#delete-comment-link", function(e) {
        e.preventDefault();

        var commentDeleteDialogTempate = Handlebars.compile($("#template-delete-comment-confirmation-dialog").html());

        $("#view-holder").append(commentDeleteDialogTempate());
        $("#delete-comment-confirmation-dialog #delete-comment-button").attr("data-comment-id", e.target.getAttribute("data-comment-id"));
        $("#delete-comment-confirmation-dialog").modal();
    })

    $("#view-holder").on("click", "#cancel-comment-button", function(e) {
        e.preventDefault();

        var deleteConfirmationDialog = $("#delete-comment-confirmation-dialog")
        deleteConfirmationDialog.modal('hide');
        deleteConfirmationDialog.remove();
    });

    $("#view-holder").on("click", "#delete-comment-button", function(e) {
        e.preventDefault();
        window.location.href = "/story/"+ $("#story-id").text() + "/comment/delete/" + e.target.getAttribute("data-comment-id");
    });
});
