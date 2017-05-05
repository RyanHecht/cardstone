let selectedName;
let selectedPriv;
let selectedFull;
let currNames;
let currLobbies;
let spectate = false;

$(document).ready(() => {
	const lobbyList = $("#lobby-table");
	
	$.get("/listLobbies", responseJSON => {
		const responseObject = JSON.parse(responseJSON);
		console.log(responseObject);
		lobbyList.empty();
		for (let i = 0, len = responseObject.length; i < len; i++) {
			const curr_lobby = responseObject[i];
			console.log(curr_lobby);

			const privateAttr = curr_lobby.private ? "Yes" : "No";
			const fullAttr = curr_lobby.full ? "Yes" : "No";
		
			const postParams = {id: curr_lobby.host};
			$.post("/username", postParams, responseJSON => {
				const hostUsername = JSON.parse(responseJSON).username;				
				lobbyList.append("<tr> <td class='name'>" 
								 + curr_lobby.name + "</td> " +
								"<td class='host'>" + hostUsername + "</td> " +
								"<td class='private'>" + privateAttr + "</td> " +
								"<td class='full'>" + fullAttr + "</td> </tr>");
			});
		}
	});

	if (error != undefined && errorHeader != undefined) {
		$("#messageModal").modal("show");
		$("#message").text(error);
		$("#messageheader").text(errorHeader);
	}
});

function drawLobbies() {
	
};

function formSubmit() {
	const form = $("<form action='/spectate' method='POST'>" +
				   " <input type='text' name='lobby' value=" +
				   selectedName + " /> />");
	$('body').append(form);
	form.submit();
};

function lobbyRequest(route, postParams, headerMsg) {
	$.post(route, postParams, responseJSON => {
		const respObj = JSON.parse(responseJSON);
		console.log(respObj);

		if (respObj.auth) {
			window.location.replace("/lobby");
		} else {
			$("#messageModal").modal("show");
			$("#messageheader").text("Error creating lobby");
			$("#message").text(respObj.message);
		}
	});
};

function spectateRequest(postParams) {
	$.post("/spectateJoin", postParams, responseJSON => {
		const respObj = JSON.parse(responseJSON);
		
		if (respObj.auth) {
			formSubmit();
		} else {
			$("#messageModal").modal("show");
			$("#messageheader").text("Error spectating lobby");
			$("#message").text(respObj.message);	
		}
	});	
};

$("#lobby-table").on("click", "tr", function() {
	selectedName = $(this).find("td.name").text();
	isFull = $(this).find("td.full").text() == "Yes";
	isPriv = $(this).find("td.private").text() == "Yes";
	
	$("#clickModal").modal("show");
	$("#click_msg").text("Would you like to join or spectate lobby " + selectedName + "?");
});

$("#join").on('click', function() {
	$("#clickModal").modal("hide");
	if (!isFull) {
		if (!isPriv) {
			const postParams = {name: selectedName, password: ""};
			console.log(postParams);
			lobbyRequest("/joinLobby", postParams, "Error joining lobby");
		} else {
			$("#passwordModal").modal("show");
			$("#pwTitle").text("Lobby " + selectedName +
							  " requires a password");
		}
	} else {
		$("#messageModal").modal("show");
		$("#messageheader").text("Error joining lobby");
		$("#message").text("Lobby " + selectedName + " is full");
	}	
});

$("#spectate").on('click', function() {
	$("#clickModal").modal("hide");
	if (!isPriv) {
		const postParams = {name: selectedName, password: ""};
		spectateRequest(postParams);
	} else {
		$("#passwordModal").modal("show");
		$("#pwTitle").text("Spectating lobby " + selectedName +
							  " requires a password");
		spectate = true;
	}
});


$("#pwSubmit").on('click', function() {
	const inputted = $("#pw").val();
	console.log(inputted);
	if (inputted != "") {
		$("#passwordModal").modal("hide");
		$("#pwSubmit").val("");
		const postParams = {name: selectedName, password: inputted};
		console.log(postParams);
		
		if (spectate) {
			spectateRequest(postParams);
		} else {
			lobbyRequest("/joinLobby", postParams, "Error joining lobby");
		}
		spectate = false;
	}
	
});

$("#hostbutton").on('click', function() {
	$("#hostLobby").modal("show");
});

$("#createlobby").on('click', function() {
	const lobby_name = $("#lname").val();
	const isPriv = $("#lprivate").is(":checked");
	const pw = $("#lpw").val();

	if (lobby_name != "") {
		$("#hostLobby").modal("hide");
		const postParams = {name: lobby_name, private: isPriv, password: pw};
		console.log(postParams);
		lobbyRequest("/makeLobby", postParams);
	}
});

$("#messageModal").on('hidden.bs.modal', function() {
	drawLobbies();
});
