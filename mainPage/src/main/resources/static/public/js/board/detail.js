async function replyPreferHandler(replyNo, preferActive, btn) {
	console.log(preferActive);
	let url = "/reply/prefer";
	let prefer = ((btn == "good") ? true : false);
	let method;
	let replyLiId = "replyLi" + replyNo;
	
	if(preferActive == null) {
		url += "/insert/" + replyNo + "/" + prefer;
		method = "post";
		//msg += "등록 성공!";
	}else if((preferActive && prefer) || (!preferActive && !prefer)) {
		url += "/delete/" + replyNo;		
		method = "delete";
	}else {
		url += "/update/" + replyNo + "/" + prefer;
		method = "put";
	}
	try{
		let res = await fetch(url, {method : method});
		if(res.status == 200) {
			let htmlText = await res.text();
			console.log(htmlText);
			document.getElementById(replyLiId).innerHTML = htmlText;
		}else if(res.status == 400) {
			alert('로그인 하세요.');
		}else{
			alert('잘못된 시도입니다.(db, server 오류)');
		}
	} catch(err) {
		alert('잘못된 시도입니다.(js 오류)');
	}
}