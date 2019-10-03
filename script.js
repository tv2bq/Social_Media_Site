function voteButton() {
	alert("vote");
}

function isEmpty() {
	var assertion = document.getElementById("assertion").value
	var justification = document.getElementById("justification").value;
	if (assertion == "" || justification == "") {
		alert ("You have to provide your opinion and state a justification before submitting");
		return false;
	}
}