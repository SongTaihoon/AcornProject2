const loginForm = document.forms.loginForm;
const findIdForm = document.forms.findIdForm;

let idSubmit = true;
let pwSubmit = true;

let namesubmit = true;
let emailsubmit = true;
let phonesubmit = true;

// 전화번호 입력 시 하이픈(-) 자동으로 생성되게 하는 함수
function phoneAutoComplete(e) {
    let number = e.value.replace(/[^0-9]/g, "");
    let phone = "";
 
    if (number.length < 4) {
         return number;
     } else if (number.length < 8) {
         phone += number.substr(0, 3);
         phone += "-";
         phone += number.substr(3);
     } else if (number.length < 9) {
         phone += number.substr(0, 3);
         phone += "-";
         phone += number.substr(3, 1);
         phone += "-";
         phone += number.substr(4, 4);
     } else if (number.length < 10) {
         phone += number.substr(0, 3);
         phone += "-";
         phone += number.substr(3, 2);
         phone += "-";
         phone += number.substr(5, 4);
     } else if (number.length < 11) {
         phone += number.substr(0, 3);
         phone += "-";
         phone += number.substr(3, 3);
         phone += "-";
         phone += number.substr(6, 4);
     } else {
		 phone += number.substr(0, 3);
         phone += "-";
         phone += number.substr(3, 4);
         phone += "-";
         phone += number.substr(7, 4);
	 }
        e.value = phone;
}

// 로그인 아이디 입력 이벤트
loginForm["user_id"].addEventListener("input", (event) => {
	let value = event.target.value;
	if(value) {
		loginForm["user_id"].classList.remove("is-invalid");
		idHelp.classList.remove("is-invalid");
		idSubmit = true;
	} else {
		idHelpInvalid.innerText = "아이디를 입력하세요.";
		loginForm["user_id"].classList.add("is-invalid");
		idHelp.classList.add("is-invalid");
		idSubmit = false;
	}
});

// 로그인 아이디 공백 차단
loginForm["user_id"].addEventListener("keydown", (event) => {
	let key = event.key;
	if(key == " ") {
		event.preventDefault();
	}
});

// 로그인 비밀번호 입력 이벤트
loginForm["user_pw"].addEventListener("input", (event) => {
	let value = event.target.value;
	if(value) {
		loginForm["user_pw"].classList.remove("is-invalid");
		pwHelp.classList.remove("is-invalid");
		pwSubmit = true;
	} else {
		pwHelpInvalid.innerText = "비밀번호를 입력하세요.";
		loginForm["user_pw"].classList.add("is-invalid");
		pwHelp.classList.add("is-invalid");
		pwSubmit = false;
	}
});

// 로그인 비밀번호 공백 차단
loginForm["user_pw"].addEventListener("keydown", (event) => {
	let key = event.key;
	if(key == " ") {
		event.preventDefault();
	}
});

// 로그인 최종 제출 이벤트
loginForm.addEventListener("submit", (event) => {
	event.preventDefault();
	if(!loginForm["user_id"].value) {
		idHelpInvalid.innerText = "필수 정보입니다.";
		loginForm["user_id"].classList.add("is-invalid");
		idHelp.classList.add("is-invalid");
		idSubmit = false;
	}
	if(!loginForm["user_pw"].value) {
		pwHelpInvalid.innerText = "필수 정보입니다.";
		loginForm["user_pw"].classList.add("is-invalid");
		pwHelp.classList.add("is-invalid");
		pwSubmit = false;
	}
	if(idSubmit && pwSubmit) {
		loginForm.submit();
	}
});

// 아이디 찾기 이름 입력 이벤트
findIdForm["user_name"].addEventListener("input", (event) => {
	let value = event.target.value;
	if(value) {
		findIdForm["user_name"].classList.remove("is-invalid");
		nameHelp.classList.remove("is-invalid");
		nameSubmit = true;
	} else {
		nameHelpInvalid.innerText = "이름을 입력하세요.";
		findIdForm["user_name"].classList.add("is-invalid");
		nameHelp.classList.add("is-invalid");
		nameSubmit = false;
	}
});

// 아이디 찾기 이름 공백 차단
findIdForm["user_name"].addEventListener("keydown", (event) => {
	let key = event.key;
	if(key == " ") {
		event.preventDefault();
	}
});

// 아이디 찾기 이름 입력 이벤트
findIdForm["user_email"].addEventListener("input", (event) => {
	let value = event.target.value;
	if(value) {
		findIdForm["user_email"].classList.remove("is-invalid");
		emailHelp.classList.remove("is-invalid");
		emailSubmit = true;
	} else {
		emailHelpInvalid.innerText = "이메일을 입력하세요.";
		findIdForm["user_email"].classList.add("is-invalid");
		emailHelp.classList.add("is-invalid");
		emailSubmit = false;
	}
});

// 아이디 찾기 이메일 공백 차단
findIdForm["user_email"].addEventListener("keydown", (event) => {
	let key = event.key;
	if(key == " ") {
		event.preventDefault();
	}
});

// 아이디 찾기 이름 입력 이벤트
findIdForm["user_phone"].addEventListener("input", (event) => {
	let value = event.target.value;
	if(value) {
		findIdForm["user_phone"].classList.remove("is-invalid");
		phoneHelp.classList.remove("is-invalid");
		phoneSubmit = true;
	} else {
		phoneHelpInvalid.innerText = "전화번호를 입력하세요.";
		findIdForm["user_phone"].classList.add("is-invalid");
		phoneHelp.classList.add("is-invalid");
		phoneSubmit = false;
	}
});

// 아이디 찾기 전화번호 공백 차단
findIdForm["user_phone"].addEventListener("keydown", (event) => {
	let key = event.key;
	if(key == " ") {
		event.preventDefault();
	}
});

// 아이디 찾기 최종 제출 이벤트
findIdForm.addEventListener("submit", (event) => {
	event.preventDefault();
	if(!findIdForm["user_name"].value) {
		nameHelpInvalid.innerText = "필수 정보입니다.";
		findIdForm["user_name"].classList.add("is-invalid");
		nameHelp.classList.add("is-invalid");
		nameSubmit = false;
	}
	if(!findIdForm["user_email"].value) {
		emailHelpInvalid.innerText = "필수 정보입니다.";
		findIdForm["user_email"].classList.add("is-invalid");
		emailHelp.classList.add("is-invalid");
		emailSubmit = false;
	}
	if(!findIdForm["user_phone"].value) {
		phoneHelpInvalid.innerText = "필수 정보입니다.";
		findIdForm["user_phone"].classList.add("is-invalid");
		phoneHelp.classList.add("is-invalid");
		phoneSubmit = false;
	}
	if(nameSubmit && emailSubmit && phoneSubmit) {
		findIdForm.submit();
	}
});