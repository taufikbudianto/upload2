$(document).ready(function () {

    function resetDimension() {
        console.log($(document).height());
        console.log($(".topbar").outerHeight());
        console.log($(".footer").outerHeight());
        height = $(document).height() - ($(".footer").outerHeight() + 4);
        $(".layout-main").css("min-height", height + "px");
    }

    resetDimension();
    $(window).resize(function () {
        resetDimension();
    });
});