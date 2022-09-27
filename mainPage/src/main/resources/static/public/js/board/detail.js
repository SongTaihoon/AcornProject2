
async function replyPreferHandler(replyNo, preferActive, btn) {
	console.log(preferActive);
	let url = "/reply/prefer";
	let prefer = ((btn == "good") ? true : false);
	//let msg = ((btn == "good") ? "좋아요" : "싫어요");

	let method;
	let replyLiId = "replyLi" + replyNo;
	
	if(preferActive == null){
		url += "/insert/" + replyNo + "/" + prefer;
		method = "post";
		//msg += "등록 성공!";
	}else if((preferActive && prefer) || (!preferActive && !prefer)){
		url += "/delete/" + replyNo;		
		method = "delete";
		//msg += "삭제 성공!";

	}else {
		url += "/update/" + replyNo + "/" + prefer;
		method = "put";
		//msg += "수정 성공!";
	}
	try{
		let res = await fetch(url, {method : method});
		if(res.status == 200){
			let htmlText = await res.text();
			document.getElementById(replyLiId).innerHTML = htmlText;
			//msgModalMsg.innerHTML = msg;
			//msgModal.show();
		}else if(res.status == 400){
			alert('로그인 하세요.');
			//msgModalMsg.innerHTML = "<a href='/user/login.do'>로그인 하세요!</a>";
			//msgModal.show();
		}else{
			alert('잘못된 시도입니다.');
			//msgModalMsg.innerHTML = "잘못된 시도입니다. 다시 시도하세요!(db, server 오류)";
			//msgModal.show();

		}
	}catch(err){
			//msgModalMsg.innerHTML = "잘못된 시도입니다. 다시 시도하세요!(js 오류)";
			//msgModal.show();
	}
}