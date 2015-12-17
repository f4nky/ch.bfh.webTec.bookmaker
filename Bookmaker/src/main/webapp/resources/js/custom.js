$(window).scroll(function() {
    if ($(this).scrollTop() > 50){ // Set position from top to add class
        $('.navbar').addClass('shrink-dark');
    } else {
        $('.navbar').removeClass('shrink-dark');
    }
});