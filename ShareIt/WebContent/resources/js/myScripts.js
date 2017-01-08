function handleLoginRequest(xhr, status, args, idDialog) {
	if (args.validationFailed || !args.logged) {
		PF(idDialog).jq.effect("shake", {
			times : 5
		}, 100);
	} else {
		PF(idDialog).hide();
		setTimeout(function() {
			$('#login-dialog-form\\:hiddenGotoHomeBtn').click();
		}, 1000);
	}
}
