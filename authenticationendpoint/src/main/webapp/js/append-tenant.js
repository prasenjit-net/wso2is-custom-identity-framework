$("#loginForm").submit(function(){
  var urlParams = new URLSearchParams(window.location.search);
  var domain = urlParams.get('tenantDomain');
  var usernameOrig = $("#username-orig").val();
  $("#username").val(usernameOrig + "@" + domain);
});