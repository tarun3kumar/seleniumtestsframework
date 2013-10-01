function copyFile(sourceFile, descFile) {
	if (browserVersion.isIE) {
		var fso = new ActiveXObject("Scripting.FileSystemObject");
		fso.CopyFile(sourceFile, descFile);
	} else {
		alert("Currenlty this function only supports Internet Explorer");
	}
}

function toggle(divId) {
	var ele = document.getElementById(divId);
	if (ele.style.display == "block") {
		ele.style.display = "none";
	} else {
		ele.style.display = "block";
	}
}
