let selectedName;
let res;

$(document).ready(() => {
	const lobbyList = $("#lobby-table");
	console.log(lobbyList);
	$.get("/listLobbies", responseJSON => {
		const responseObject = JSON.parse(responseJSON);
		console.log(responseObject);
		
		for (let i = 0, len = responseObject.length; i < len; i++) {
			const curr_lobby = responseObject[i];
			console.log(curr_lobby);
			
			const privateAttr = curr_lobby.private ? "Yes" : "No";
			const fullAttr = curr_lobby.full ? "Yes" : "No";
			
			lobbyList.append("<tr class='clickable-row'> <td>" 
							 + curr_lobby.name + "</td> " +
							" <td>" + curr_lobby.host + "</td> " +
							"<td>" + private + "</td> " +
		    				"<td>" + full + "</td> </tr>");
		}
	});
	
	$(".clickable-row").on('click', function() {
		const isFull = $(this).find("td.full").text() == "Yes";
		console.log("isFull " + isFull);
		
		const isPriv = $(this).find("td.private").text() == "Yes";
		console.log("isPriv " + isPriv);
		
		selectedName = $(this).find("td.name").text();
	
		if (!isFull) {
			if (!isPriv) {
				const postParams = {name: selectedName, password: ""};
				console.log(postParams);
				$.post("/joinLobby", postParams, function() {
				});
			} else {
				$("#passwordModal").modal("show");
				$("#pwTitle").text("Lobby " + selectedName + 
								  " requires a password");
			}
		} else {
			$("#messageModal").modal("show");
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
			$.post("/joinLobby", postParams, function() {
			});
		}
	});
	
	$("#hostbutton").on('click', function() {
		$("#hostLobby").modal("show");
	});
	
	$("#createlobby").on('click', function() {
		console.log("I was clickeddddddddddddd");
		const lobby_name = $("#lname").val();
		const isPriv = $("#lprivate").is(":checked");
		const pw = isPriv ? $("#lpw").val() : "";
		if (lobby_name != "") {
			$("#hostLobby").modal("hide");
			const postParams = {name: lobby_name, private: isPriv, password: pw};
			console.log(postParams);
			$.post("/makeLobby", postParams, responseJSON => {
				const respObj = JSON.parse(responseJSON);
				console.log(respObj);
				console.log("oyyyyyyyyyyyyyyyuyyyyyyyyyyyyyyy");
				
				console.log(respObj.auth);
				if (respObj.auth) {
					window.location.replace("/login");
				} else {
					console.log("Quality you can taste");
				}
//				if (respObj.auth) {
//					window.location.replace("/login");
//				} else {
//					$("#messageModal").modal("show");
//					$("#message").text(respObj.message);
//				}
			});
			$("#messageModal").modal("show");
			console.log("blah");
		} else {
			console.log("empty");
		}
	});
});