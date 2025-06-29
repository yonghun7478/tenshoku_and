안드로이드 'お気に入り' 화면 개발 요청 프롬프트

구현 대상 화면:
화면 이름: 'お気に入り' (즐겨찾기) 화면
목적: 사용자가 다른 화면에서 '마음에 듦'으로 표시한 다른 사용자들의 목록을 보여주는 화면입니다.

데이터 요구사항:
화면에 표시될 각 사용자 정보는 다음 필드를 포함하는 데이터 모델(예: FavoriteUserInfo)로 정의되어야 합니다.

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

-------------------------------------------

코드 가이드

#### 1.1. UI (Composable Functions)

* **책임:**
    * ViewModel로부터 UI 상태(State)를 전달받아 화면에 표시합니다. (State Hoisting)
    * 사용자 입력을 이벤트 람다(Lambda)를 통해 ViewModel에 전달합니다.
    * Composable 함수는 상태를 직접 가지지 않고, 상태 변화에 따라 Recomposition을 통해 UI를 업데이트합니다.
    * 화면 전환(Navigation) 로직을 처리합니다. (Compose Navigation 사용 권장)
    * View와 관련된 로직(애니메이션, 리소스 로딩 등)만 처리합니다.
* **규칙:**
    * **비즈니스 로직 금지:** Composable 함수는 데이터 처리나 비즈니스 로직을 포함하지 않아야 합니다. 모든 로직은 ViewModel 또는 Domain Layer로 위임합니다.
    * **상태 호이스팅 (State Hoisting):** Composable은 상태를 직접 관리하기보다 상위 Composable이나 ViewModel로부터 상태를 전달받고, 이벤트를 전달하는 방식으로 구현합니다. (`Stateful` Composable 최소화)
    * **ViewModel 참조:** 일반적으로 최상위 Composable (Screen 단위) 또는 Navigation Graph 범위에서 Hilt를 통해 주입된 ViewModel 인스턴스를 참조합니다. (`hiltViewModel()` 사용)
    * **상태 관찰:** ViewModel에서 제공하는 `StateFlow`를 `collectAsStateWithLifecycle()` (또는 `collectAsState()`)를 사용하여 관찰하고, 이를 하위 Composable에 필요한 상태로 전달합니다.
    * **이벤트 전달:** 사용자 입력(클릭 등)은 ViewModel의 함수를 호출하는 람다 표현식(e.g., `onClick = { viewModel.someAction(data) }`)을 통해 전달합니다.
    * **리소스 접근:** 문자열, 색상, 크기 등은 Compose의 리소스 API (`stringResource`, `colorResource`, `dimensionResource` 등)를 통해 접근합니다.
