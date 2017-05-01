let selectedName;
let selectedRow;

$(document).ready(() => {
	const lobbyList = $("#lobby-table");
//	setInterval(function() {
//		$.get("/listLobbies", responseJSON => {
//			const responseObject = JSON.parse(responseJSON);
//			console.log(responseObject);
//			lobbyList.empty();
//
//			for (let i = 0, len = responseObject.length; i < len; i++) {
//				const curr_lobby = responseObject[i];
//				console.log(curr_lobby);
//
//				const privateAttr = curr_lobby.private ? "Yes" : "No";
//				const fullAttr = curr_lobby.full ? "Yes" : "No";
//
//				const postParams = {id: curr_lobby.host};
//				$.post("/username", postParams, responseJSON => {
//					const hostUsername = JSON.parse(responseJSON).username;				
//					lobbyList.append("<tr> <td class='name'>" 
//									 + curr_lobby.name + "</td> " +
//									" <td class='host'>" + hostUsername + "</td> " +
//									"<td class='private'>" + privateAttr + "</td> " +
//									"<td class='full'>" + fullAttr + "</td> </tr>");
//				});
//			}
//		});
//	}, 2000);
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

$("#lobby-table").on("click", "tr", function() {
	console.log($(this));
	const isFull = $(this).find("td.full").text() == "Yes";
	console.log("isFull " + isFull);

	const isPriv = $(this).find("td.private").text() == "Yes";
	console.log("isPriv " + isPriv);

	selectedName = $(this).find("td.name").text();
	selectedRow = $(this);
	$("#clickModal").modal("show");
	$("#click_msg").text("Would you like to join or spectate lobby " + selectedName + "?");
});

$("#join").on('click', function(event) {
	console.log(event);
	console.log($(this));
	
	const isFull = selectedRow.find("td.full").text() == "Yes";
	const isPriv = selectedRow.find("td.private").text() == "Yes";
	
	$("#clickModal").modal("hide");
	if (!isFull) {
		if (!isPriv) {
			const postParams = {name: selectedName, password: ""};
			console.log(postParams);
			$.post("/joinLobby", postParams, responseJSON => {
				const respObj = JSON.parse(responseJSON);
				console.log(respObj);

				if (respObj.auth) {
					window.location.replace("/lobby");
				} else {
					$("#messageModal").modal("show");
					$("#messageheader").text("Error joining lobby");
					$("#message").text(respObj.message);
				}
			});
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


$("#pwSubmit").on('click', function() {
	const inputted = $("#pw").val();
	console.log(inputted);
	if (inputted != "") {
		$("#passwordModal").modal("hide");
		console.log(selectedName);
		const postParams = {name: selectedName, password: inputted};
		console.log(postParams);
		$.post("/joinLobby", postParams, responseJSON => {
			const respObj = JSON.parse(responseJSON);
			console.log(respObj);

			if (respObj.auth) {
				window.location.replace("/lobby");
			} else {
				$("#messageModal").modal("show");
				$("#messageheader").text("Error joining lobby");
				$("#message").text(respObj.message);
			}
		});
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
		$.post("/makeLobby", postParams, responseJSON => {
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
	}
});
