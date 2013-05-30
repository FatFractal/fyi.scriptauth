var ff = require('ffef/FatFractal');

exports.cleanup = function() {
    var count = 0;
    var movies = ff.getArrayFromUri("/Movies");
    if (movies == null) return;

    for (var i = 0; i < movies.length; i++) {
        ff.deleteObj(movies[i]);
        count++;
    }
    var actors = ff.getArrayFromUri("/Actors");
    if (actors == null) return;

    for (var i = 0; i < actors.length; i++) {
        ff.deleteObj(actors[i]);
        count++;
    }
    var favs = ff.getArrayFromUri("/Favs");
    if (favs == null) return;

    for (var i = 0; i < favs.length; i++) {
        ff.deleteObj(favs[i]);
        count++;
    }
    var r = ff.response();
    r.result = "<h1> Thanks for visiting</h1><p>We have deleted  " + count + " objects from the tests.</p>";
    r.responseCode="200";
    r.statusMessage = "cleanup has deleted " + count + " objects from your backend.";
    r.mimeType = "text/html";
}

