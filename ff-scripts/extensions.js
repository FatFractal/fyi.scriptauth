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

exports.FacebookLogin = function() {
    var token = new Token("AAAGsWoXdMCgBAH0cDVCrdHymFLXoxy8ymMQK1Cx2x6OdTO3xJtB9v2JwEW26QP5JX32atrS7uMUU8abNtgfQgLL98L0ZD");
    var scriptAuthService = "FACEBOOK";
    var r = ff.response();
    ff.setTokenForScriptAuthService(scriptAuthService, token);
    ff.loginWithScriptAuthService(scriptAuthService, function(result) {
        r.responseCode="200";
        r.result = result;        
        r.statusMessage = "Facebook auth succeeded.";
    }, function(code, msg) {
        r.responseCode=code;
        r.result = msg;    	   
        r.statusMessage = "Facebook auth failed.";
    });
    r.mimeType = "text/html";
};

exports.TwitterLogin = function() {
    var token = new Token("372410927-EFx4MiGYuhssJxiSkwjdKsGNpAJQZFTuqCds7SY",
                          "N6kNGZucK4GymhhWtLPMoJLmvS8bIVVb2s7NCvVN5Q");
    var scriptAuthService = "TWITTER";
    var r = ff.response();
    ff.setTokenForScriptAuthService(scriptAuthService, token);
    ff.loginWithScriptAuthService(scriptAuthService, function(result) {
        r.responseCode="200";
        r.result = result;        
        r.statusMessage = "Twitter auth succeeded.";
    }, function(code, msg) {
        r.responseCode=code;
        r.result = msg;    	   
        r.statusMessage = "Twitter auth failed.";
    });
    r.mimeType = "text/html";
};

