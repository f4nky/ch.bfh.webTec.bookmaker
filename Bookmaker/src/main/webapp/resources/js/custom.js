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