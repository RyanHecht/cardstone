let selectedName;
let selectedPriv;
let selectedFull;
let currNames;
let currLobbies;
let spectate = false;
let isTutorial = false;

let tutorialHandler;
const stage = TutorialEnum.LOBBIES;

$(document).ready(() => {
	const tutorial_stage = tutorialStage();
	if (tutorial_stage >= 0) {
		tutorialHandler = new TutorialHandler(stage, user_prompt, next_stage, disabled_inputs);
		tutorialHandler.handle(tutorial_stage);
		$("#createlobby").on("click", function() {
			tutorialHandler.action_complete();
		});
		isTutorial = true;
	}


	const listSocket = new WebSocket("ws://" + window.location.host + "/lobbyListSocket");
	listSocket.onopen = function() {
		console.log("opened list socket");

		setInterval(function() {
      listSocket.send("lubdub");
			//console.log("sent heartbeat")
    }, 5000);
	}
	listSocket.onmessage = function(message) {
		drawLobbies(JSON.parse(message.data));
	};

	$.get("/listLobbies", responseJSON => {
		const responseObject = JSON.parse(responseJSON);
		drawLobbies(responseObject);
	});

	if (undefined != error && undefined != errorHeader) {
		$("#messageModal").modal("show");
		$("#message").text(error);
		$("#messageheader").text(errorHeader);
	}

	function drawLobbies(responseObject) {
		const lobbyList = $("#lobby-table");
		lobbyList.empty();
		for (let i = 0, len = responseObject.length; i < len; i++) {
			const curr_lobby = responseObject[i];

			const privateAttr = curr_lobby.private ? "Yes" : "No";
			const fullAttr = curr_lobby.full ? "Yes" : "No";

			const postParams = {id: curr_lobby.host};
			$.post("/username", postParams, responseJSON => {
				const hostUsername = JSON.parse(responseJSON).username;
				lobbyList.append("<tr><td class='name borderWhite'>"
								 + curr_lobby.name + "</td> " +
								"<td class='host borderWhite'>" + hostUsername + "</td> " +
								"<td class='private borderWhite'>" + privateAttr + "</td> " +
								"<td class='full borderWhite'>" + fullAttr + "</td></tr>");
			});
		}
	};
});

function user_prompt() {
	$("#messageModal").modal("show");
	$("#messageheader").text("You're doing great so far!");
	$("#message").text("This is the lobbies page. As a player, you can click on any row of the lobbies table, and be given the option to spectate or join the selected lobby. You can also create a lobby of your own. Lobbies can be password-protected or public. Anyway, let's try and make a lobby by clicking on the create lobby button below.");
};

function disabled_inputs() {
	$("#navbar").find("a").removeAttr("href");
	$("#logolink").removeAttr("href");
	$("#hostbutton").addClass("tutorialHighlight");
};

function next_stage() {
	window.location.replace("/tutorial_lobby");
};



function formSubmit() {
	const form = $("<form action='/spectate' method='POST'>" +
				   " <input type='text' name='lobby' value=" +
				   selectedName + " /> />");
	$('body').append(form);
	form.submit();
};

function lobbyRequest(route, postParams, headerMsg) {
	console.log("making request to " + route);
	console.log("have params " + postParams);
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
	if (!isTutorial) {
		selectedName = $(this).find("td.name").text();
		isFull = $(this).find("td.full").text() == "Yes";
		isPriv = $(this).find("td.private").text() == "Yes";

		$("#clickModal").modal("show");
		$("#click_msg").text("Would you like to join or spectate lobby " + selectedName + "?");
	}
});

$("#join").on('click', function() {
	console.log("I clicked join!");
	$("#clickModal").modal("hide");
	if (!isFull) {
		if (!isPriv) {
			const postParams = {name: selectedName, password: ""};
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
	if (inputted != "") {
		$("#passwordModal").modal("hide");
		$("#pwSubmit").val("");
		const postParams = {name: selectedName, password: inputted};

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
	if (!isTutorial) {
		const lobby_name = $("#lname").val();
		const isPriv = $("#lprivate").is(":checked");
		const pw = $("#lpw").val();

		if (lobby_name != "") {
			$("#hostLobby").modal("hide");
			const postParams = {name: lobby_name, private: isPriv, password: pw};
			lobbyRequest("/makeLobby", postParams);
		}
	}
});

$("#messageModal").on('hidden.bs.modal', function() {
	drawLobbies();
});
