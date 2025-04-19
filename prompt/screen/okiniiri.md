안드로이드 'お気に入り' 화면 개발 요청 프롬프트
프로젝트 개요:

앱 종류: 안드로이드 매칭(소개팅) 애플리케이션

주요 기술 스택:

언어: Kotlin

아키텍처: Android Clean Architecture (Data, Domain, Presentation 레이어 분리)

UI: Jetpack Compose

DI: Hilt

구현 대상 화면:

화면 이름: 'お気に入り' (즐겨찾기) 화면

목적: 사용자가 다른 화면에서 '마음에 듦'으로 표시한 다른 사용자들의 목록을 보여주는 화면입니다.

데이터 요구사항:

화면에 표시될 각 사용자 정보는 다음 필드를 포함하는 데이터 모델(예: FavoriteUserInfo)로 정의되어야 합니다.

섬네일 이미지 URL (String)

이름 (String)

나이 (Int 또는 String)

사는 곳 (String)

키 (Int 또는 String)

직업 (String)

동거인 유무/정보 (String 또는 Enum)

형제자매 유무/정보 (String 또는 Enum)

혈액형 (String 또는 Enum)

이 사용자 목록 데이터는 Clean Architecture에 따라 Data 레이어(로컬 DB 또는 원격 API)에서 가져와 Domain 레이어를 거쳐 Presentation 레이어(ViewModel)로 전달되어야 합니다.

UI 구성 요구사항 (첨부된 스크린샷 PXL_20250417_151413134.jpg 참조):

전체 구조: Scaffold 사용

TopAppBar:

왼쪽: 뒤로 가기 아이콘 버튼 (NavigationIcon)

중앙: "お気に入り" 제목 (Text)

오른쪽: 더 보기 아이콘 버튼 (ActionIcon)

본문: LazyColumn을 사용하여 스크롤 가능한 사용자 목록 표시 (적절한 contentPadding 및 verticalArrangement 적용).

사용자 목록 아이템 (Card 컴포저블):

둥근 모서리(RoundedCornerShape)와 약간의 그림자(elevation) 적용.

내부 구조 (Column):

이미지 영역 (Box):

AsyncImage (Coil 등): 사용자 섬네일 표시 (가로 꽉 참, 정사각형 비율, 둥근 모서리).

이미지 우측 상단 오버레이 (Box 내 Alignment.TopEnd): 사진 개수 등 추가 정보 표시 (스크린샷의 '12' 참고).

정보 영역 (Column): 이미지 하단, 패딩 적용.

기본 정보 (Row): 아이콘 + 이름, 나이, 아이콘 + 사는 곳 표시.

상세 정보 (Column 또는 FlowRow): 키, 직업, 동거인, 형제자매, 혈액형 등 나머지 모든 명시된 정보를 적절히 배치하여 표시 (정보량이 많으므로 가독성 좋은 레이아웃 필요).

액션 버튼 (Button): "みてね！" 텍스트, 주황색 배경, 둥근 모서리, 가로 꽉 참.

사용자 인터랙션:
즐겨찾기 목록 조회.
무한스크롤 기능 
풀다운기능을 활용한 리프레시 기능