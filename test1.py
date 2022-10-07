#전처리
#https://lsjsj92.tistory.com/350?category=792966
#ex)
#이런식으로 문구변경가능
from typing import Sequence
dic = {
	" before " : " after ",
	" before " : " after ",
}
#한글 영어 숫자 아닌 값, 특수문자 제거
content = re.sub('[^a-zA-Z0-9 ㄱ-ㅣ가-힣]', '', content)


#해당 루트의 모든 엑셀파일 돌면서 참조
files = glob.glob(root_dir+"/*/*.xlsx", recursive=True)
for i in files:
	file_to_ids(i)


#BOW생성. json형태로 저장?
word_dic = {"_MAX":0}
def text_to_ids(text):
		text = text.strip()
		words = text.split(",")
		result = []
		for n in words:
			n = n.strip()
			if n == "": continue
			if not n in word_dic:
				#단어 사전에 단어가 없으면 만든다는 의미. 처음 시작하면 word_dic[max]는 0. 이게 하나씩 증가하면서 각 단어에 대한 id값 생성으로 보면 됨.
				wid = word_dic[n] = word_dic["_MAX"]
				print("4. text_to_ids의 단어 사전에 단어가 없을 때 : " +n +" /// "+str(wid) + " 그리고 : " + str(word_dic[n]))
				word_dic["_MAX"] +=1
			else:
				#단어 사전에 단어가 있으면, 넣는다.
				wid = word_dic[n]

data = json.load(open("./file.json"))

#Dense=층 쌓기. relu = 활성화함수. softmax=출력활성화함수
X_train, X_test, Y_train, Y_test = train_test_split(X, Y, test_size=0.2, random_state=seed)
model = Sequential()
model.add(Dense(128, input_dim=max_words, activation='relu'))
model.add(Dense(nb_classes, activation="softmax"))
model.complie(loss='categorical_crossentropy', optimizer='adam', metrics=['accuracy'])