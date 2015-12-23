$(window).scroll(function() {
    if ($(this).scrollTop() > 50){ // Set position from top to add class
        $('.navbar').addClass('shrink-dark');
    } else {
        $('.navbar').removeClass('shrink-dark');
    }
});

$('.table tr[data-href]').each(function() {
    $(this).hover(
        function() {
            $(this).addClass('active');
        },
        function() {
            $(this).removeClass('active');
        }).click( function(){
            document.location = $(this).attr('data-href');
        }
    );
});

$('.table tr[data-toggle]').each(function() {
    $(this).hover(
        function() {
            $(this).addClass('active');
        },
        function() {
            $(this).removeClass('active');
        }
    )
});

//Event for the ajax request to display a progress icon
jsf.ajax.addOnEvent(function(data) {
    var ajaxstatus = data.status;
    var ajaxloader = document.getElementById("ajaxloader");

    switch (ajaxstatus) {
        case "begin":
            ajaxloader.style.display = 'block';
            break;

        case "complete":
            ajaxloader.style.display = 'none';
            break;

        case "success":
            break;
    }
});
jsf.ajax.addOnEvent(function(data) {
    var ajaxstatus = data.status;
    var ajaxloader = document.getElementById("ajaxloader2");

    switch (ajaxstatus) {
        case "begin":
            ajaxloader.style.display = 'block';
            break;

        case "complete":
            ajaxloader.style.display = 'none';
            break;

        case "success":
            break;
    }
});