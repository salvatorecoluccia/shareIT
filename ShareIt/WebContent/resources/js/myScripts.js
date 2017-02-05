function handleLoginRequest(xhr, status, args, idDialog) {
	console.log("handleLoginRequest started");
	if (args.validationFailed || !args.logged) {
		PF(idDialog).jq.effect("shake", {
			times : 5
		}, 100);
	} else {
		PF(idDialog).hide();
		setTimeout(function() {
			$('#login-dialog-form\\:hiddenGotoHomeBtn').click();
		}, 200);
	}
}

function onSignIn(googleUser) {
	console.log("onSignIn started");
	var id_token = googleUser.getAuthResponse().id_token;
	var username = googleUser.getBasicProfile().getGivenName() + "_"
			+ googleUser.getBasicProfile().getEmail();
	var password = "GOOGLE_STORED"
	console.log("IdToken= " + id_token);
	console.log("username=" + username);
	console.log("password=" + password);
	document.getElementById('login-dialog-form:hiddenTokenInput').value = id_token;
	document.getElementById('login-dialog-form:username').value = username;
	document.getElementById('login-dialog-form:password').value = password;
	if (googleUser.isSignedIn) {
		$('#login-dialog-form\\:signInButton').click();
	}
}

/*
 * function onSignInNew(){ console.log("onSignInNew started");
 * gapi.auth2.signIn().then(function(){ var myGoogleUser =
 * gapi.auth2.currentUser.get(); onSignIn(myGoogleUser); }); }
 */


function doGoogleLogout() {
	console.log("doGoogleLogout:logging out from google..");
	gapi.load('auth2', function() {
		if (gapi == undefined || gapi.auth2 == undefined) {
			console.log("doGoogleLogout:User is not sign in google");
			$('.trueLogoutBtn').click();
			return;
		}
		gapi.auth2
		.init(
				{
					client_id : '525721307601-q7ahh9jk2e3ng7nd42o5tllovipo849t.apps.googleusercontent.com'
				}).then(function() {
			var auth2 = gapi.auth2.getAuthInstance();
			if (auth2 == undefined) {
				console.log("doGoogleLogout:User is not sign in google");
				$('.trueLogoutBtn').click();
				return;
			}
			auth2.signOut().then(function() {
				console.log('doGoogleLogout:User signed out.');
				$('.trueLogoutBtn').click();
			});
		});
      });
	
	
	
};
