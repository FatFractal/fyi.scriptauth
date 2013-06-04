var ff = new FatFractal();
// init stuff
var callbackUri = ff.getBaseUrl() + "authorize.html";
console.log("setting callbackUri to: " + callbackUri);
sessionStorage.setItem("ff-callback-uri", callbackUri);
ff.setCallbackUriForScriptAuthService(ff.SCRIPT_AUTH_SERVICE_FACEBOOK, callbackUri);
ff.setCallbackUriForScriptAuthService(ff.SCRIPT_AUTH_SERVICE_TWITTER, callbackUri);

function cleanup(e) {
   var leave_message = 'Thanks for visiting! \nAll test data has been deleted.';
   ff.getObjFromExtension("/ff/ext/cleanup", function(resp) {
   }, function(code, msg) {
      console.error("cleanup error: " + msg);
   });
   return leave_message;
}
//window.onbeforeunload=cleanup;

function logout() {
    ff.logout(function(result) {
         console.log("User was logged out: " + JSON.stringify(result));
         var el = document.getElementById("loggedInUserName");
         var bel = document.getElementById("logoutButton");
         bel.style.display = "none";
         el.innerHTML = "";
    }, function(code, msg) {
         console.error("logout() Error: " + code + " " + msg);
    });
}
function Movie(obj) {
   this.title = null;
   this.description = null;
   this.year = null;
   if(obj) {
      this.title = obj.title;
      this.description = obj.description;
      this.year = obj.year;
   }
   return this;
}
function facebookLogin() {
    ff.authUriForScriptAuthService(ff.SCRIPT_AUTH_SERVICE_FACEBOOK, function(authUri) {
        sessionStorage.setItem("ff-auth-provider", "FACEBOOK");
        window.location = authUri;
    }, function(code, msg) {
         console.error("facebookLogin() Error: " + code + " " + msg);
    });
}
function twitterLogin() {
    ff.authUriForScriptAuthService(ff.SCRIPT_AUTH_SERVICE_TWITTER, function(authUri) {
        sessionStorage.setItem("ff-auth-provider", "TWITTER");
        var requestToken = ff.getRequestTokenForScriptAuthService(ff.SCRIPT_AUTH_SERVICE_TWITTER);
        sessionStorage.setItem("ff-tw-request-token", JSON.stringify(requestToken));
        window.location = authUri;
    }, function(code, msg) {
         console.error("twitterLogin() Error: " + code + " " + msg);
    });
}
function saveMovie() {
   var el = document.getElementById('movie-movies-response');
   var m = new Movie();
   m.title = "Argo";
   m.description = "Argo is a 2012 historical drama thriller film directed by Ben Affleck.";
   m.year =2012;
   ff.createObjAtUri(m, "/Movies", function(resp) {
      var mov = new Movie(resp);
      var str = js_beautify(JSON.stringify(mov), {
         indent_size: 4,
         indent_char: '&nbsp;',
         linefeed_char: '<br>'
      });
      el.innerHTML = "<br><div class = 'well blue'>Movie was created in the  " +
         resp.ffRL + " Collection with data: " +
         str + "</div>";
      console.log("Movie was created: " + JSON.stringify(resp));
   }, function(code, msg) {
      console.error("saveMovie() createObjAtUri Error: " + code + " " + msg);
      el.innerHTML = "<br><div class = 'well red'>Got an error: " + msg + "</div>";
   });
}

