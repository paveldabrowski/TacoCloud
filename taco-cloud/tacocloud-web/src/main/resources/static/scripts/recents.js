$(document).ready(function() {
  $.ajax({
    url : "/design/recent"
  }).then(function(data) {
    if (data.length > 0) {
      $.get('mustache/recents.mst', function(template) {
        var rendered = Mustache.render(template, data);
        $('#recents').html(rendered);
      });
    } else {
      $('#recents').html("<p>Nie przygotowano jeszcze żadnych tacos..." +
          "bądź pierwszym, który utworzy własne taco!</p>");
    }
  });
});