/**
 * Created by JQ棣 on 26/10/2017.
 */
// Datepicker
$(function () {
    $(".datepicker").each(function (i, el) {
        $(el).datepicker().on('hide', function(event) {
            event.preventDefault();
            event.stopPropagation();
        });
    });
});