var ff = require('ffef/FatFractal');

exports.cleanup = function() {
    var count = 0;
    var movies = ff.getArrayFromUri("/Movies");
    if (movies == null) return;

    for (var i = 0; i < movies.length; i++) {
        ff.deleteObj(movies[i]);
        count++;
    }
    var r = ff.response();
    r.result = "<h1> Thanks for visiting</h1><p>We have deleted  " + count + " objects from the tests.</p>";
    r.responseCode="200";
    r.statusMessage = "cleanup has deleted " + count + " objects from your backend.";
    r.mimeType = "text/html";
}

