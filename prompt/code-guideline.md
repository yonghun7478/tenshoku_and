## Android 클린 아키텍처 코드 규칙 정의

이 문서는 클린 아키텍처 원칙에 따라 Android 애플리케이션을 개발하기 위한 코드 규칙을 정의합니다.

### 1. Presentation Layer (표현 계층)

* **역할:** UI 로직 및 상태 관리, 사용자 이벤트 처리
* **기술 스택:** Jetpack Compose (UI), Hilt (의존성 주입)
* **의존성:** Domain Layer에만 의존합니다. Data Layer에 직접 접근하지 않습니다.

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

#### 1.2. ViewModel

* **책임:**
    * UI에 필요한 상태(State)를 관리하고 `StateFlow` 등을 통해 노출합니다.
    * UI(Composable)로부터 이벤트를 받아 처리하고, 필요한 경우 UseCase를 호출합니다.
    * UseCase 실행 결과를 바탕으로 UI 상태를 업데이트합니다.
* **규칙:**
    * **Hilt 의존성 주입:** `@HiltViewModel` 어노테이션을 사용하여 Hilt로부터 의존성(UseCase 등)을 주입받습니다.
    * **Android 프레임워크 의존성 최소화:** Context 등 Android 프레임워크 클래스에 대한 직접적인 참조를 피해야 합니다. (단, `Application` Context가 필요한 경우 `@HiltViewModel`과 함께 `Application`을 주입받아 사용)
    * **UseCase 사용:** 비즈니스 로직 처리는 반드시 UseCase를 통해 수행합니다. ViewModel이 직접 Repository에 접근하지 않습니다.
    * **상태 노출:** UI 상태는 `StateFlow`를 사용하여 외부에 노출합니다. 변경 불가능한 타입(Immutable type)으로 노출하는 것을 권장합니다. (`asStateFlow()` 사용)
    * **코루틴 활용:** 비동기 작업(UseCase 호출 등)은 코루틴(`viewModelScope`)을 사용하여 관리합니다.
    * **상태 홀더:** UI에 필요한 데이터는 하나의 상태 홀더 데이터 클래스(e.g., `ScreenUiState`)로 묶어 관리하는 것을 권장합니다.
    * **오류 처리:** UseCase 실행 중 발생할 수 있는 오류를 처리하고, 이를 UI 상태에 반영하여 사용자에게 적절한 피드백(오류 메시지 등)을 제공합니다.

### 2. Domain Layer (도메인 계층)

* **역할:** 핵심 비즈니스 로직 정의 및 실행
* **의존성:** 다른 레이어에 의존하지 않습니다. 순수한 Kotlin/Java 모듈로 구성됩니다. (Presentation, Data Layer로부터 독립적)
* **구성 요소:** UseCase, Domain Model (Entity), Repository Interface

#### 2.1. UseCase (Interactor)

* **책임:**
    * 단일 비즈니스 로직(Use Case)을 캡슐화합니다.
    * 필요한 Repository 인터페이스를 통해 데이터를 요청하고, 가공하여 결과를 반환합니다.
* **규칙:**
    * **단일 책임 원칙:** UseCase 클래스는 하나의 특정 기능만 수행해야 합니다.
    * **명명 규칙:** `동사 + 명사 + UseCase` 형식 (e.g., `GetUserListUseCase`, `SubmitFormUseCase`)을 따릅니다.
    * **호출 방식:** `invoke` 연산자를 오버로딩하여 함수처럼 호출할 수 있도록 구현하는 것을 권장합니다. (`suspend operator fun invoke(...)`)
    * **Repository 인터페이스 사용:** Data Layer의 구체적인 구현이 아닌, Domain Layer에 정의된 Repository 인터페이스를 통해 데이터에 접근합니다.
    * **비즈니스 로직 집중:** 데이터 소스나 UI와 관련된 로직은 포함하지 않고, 순수 비즈니스 규칙 처리에만 집중합니다.

#### 2.2. Domain Model (Entity)

* **책임:** 애플리케이션의 핵심 데이터 구조 정의
* **규칙:**
    * **순수 데이터 클래스:** 비즈니스 로직을 포함하지 않는 순수한 데이터 구조(e.g., Kotlin `data class`)로 정의합니다.
    * **플랫폼 독립성:** 특정 플랫폼(Android)이나 데이터 소스(DB, Network)에 대한 의존성을 갖지 않습니다.

#### 2.3. Repository Interface

* **책임:** 데이터 소스에 대한 접근 방법을 정의하는 계약(Contract) 역할
* **규칙:**
    * Data Layer에서 이 인터페이스를 구현합니다.
    * Domain Layer의 UseCase는 이 인터페이스를 통해 데이터 작업을 수행합니다.
    * 메서드는 Domain Model을 반환하거나 사용해야 합니다.

### 3. Data Layer (데이터 계층)

* **역할:** 데이터 소스(네트워크, 데이터베이스, 파일 등) 관리 및 데이터 제공/저장 로직 구현
* **의존성:** Domain Layer에만 의존합니다. (Domain Model, Repository Interface)
* **구성 요소:** Repository Implementation, Data Source (Remote/Local), Data Model (DTO), Hilt Module (의존성 주입 설정)

#### 3.1. Repository Implementation

* **책임:**
    * Domain Layer에 정의된 Repository 인터페이스를 구현합니다.
    * 하나 이상의 Data Source(Remote, Local)를 사용하여 필요한 데이터를 가져오거나 저장합니다.
    * Data Source로부터 받은 Data Model(DTO) 또는 더미 데이터 구조를 Domain Model(Entity)로 변환(Mapping)합니다.
* **규칙:**
    * **Hilt 의존성 주입:** 생성자 주입(`@Inject constructor`)을 사용하여 필요한 의존성(Data Source, Mapper 등)을 주입받습니다. Repository 인터페이스와 구현체를 바인딩하기 위해 Hilt 모듈(`@Module`, `@Binds`)을 사용합니다.
    * **인터페이스 구현:** Domain Layer의 Repository 인터페이스를 구현해야 합니다.
    * **Data Source 사용:** 필요한 데이터를 얻기 위해 항상 Remote Data Source 또는 Local Data Source 인터페이스를 통해 접근합니다. (DataSource 구현체 내부에서 초기 개발 시 더미 데이터를 반환할 수 있습니다.)
    * **데이터 매핑:** DataSource로부터 받은 데이터(DTO 또는 더미 데이터 구조)를 UseCase가 사용할 수 있도록 Domain Model(Entity)로 변환합니다. Mapper 클래스를 별도로 두는 것을 권장합니다.
    * **오류 처리:** DataSource 접근 시 발생할 수 있는 오류(네트워크 오류, DB 오류, 또는 더미 데이터 소스에서 정의한 오류 시나리오 등)를 처리하고, 적절한 방식으로 상위 레이어(UseCase)에 전달합니다. (e.g., `Flow`의 `catch` 연산자, Result Wrapper 클래스 사용)
    * **단일 진실 공급원 (Single Source of Truth):** 실제 데이터 소스가 구현되면, 로컬 캐싱 전략 등을 사용하여 데이터의 일관성을 유지하는 것을 고려합니다.

#### 3.2. Data Source (Remote/Local)

* **책임:**
    * 특정 데이터 소스(네트워크 API, Room 데이터베이스, DataStore 등)와의 직접적인 통신 또는 더미 데이터 생성을 담당합니다.
    * 데이터 소스 인터페이스에 정의된 계약에 따라 데이터를 반환합니다. (초기 개발 시에는 이 구현체 내부에서 더미 데이터를 생성하여 반환할 수 있습니다.)
* **규칙:**
    * **Hilt 의존성 주입:** 인터페이스와 구현체로 분리하고 Hilt 모듈을 통해 의존성을 주입하는 것을 권장합니다. (예: Retrofit Service, Room DAO 등)
    * **구체적인 기술 구현:** Retrofit, Room, DataStore 등 특정 데이터 접근 기술을 사용하거나, 초기 개발 시에는 더미 데이터를 생성하는 로직을 포함하여 구현합니다.
    * **데이터 모델(DTO) 사용:** 해당 데이터 소스에 특화된 데이터 모델(DTO - Data Transfer Object)을 사용합니다. (예: 네트워크 응답 모델, DB 테이블 모델) 더미 데이터를 생성할 때도 이 DTO 구조를 활용할 수 있습니다.
    * **오류 처리:** API 호출 실패, DB 쿼리 실패 등 각 데이터 소스에서 발생 가능한 구체적인 오류를 처리하거나, 더미 데이터 생성 로직 내에서 특정 오류 시나리오를 시뮬레이션할 수 있습니다. (Exception을 던지거나 Result 타입을 반환)

#### 3.3. Data Model (DTO - Data Transfer Object)

* **책임:** 특정 데이터 소스와의 통신에 사용되는 데이터 구조 정의. (더미 데이터 단계에서도 구조 정의에 활용 가능)
* **규칙:**
    * **데이터 소스 종속성:** 특정 데이터 소스(네트워크 라이브러리 어노테이션, DB 어노테이션 등)에 대한 의존성을 가질 수 있습니다.
    * **Domain Model과의 분리:** Domain Model(Entity)과 분리하여 관리합니다. 이는 데이터 소스의 변경이 Domain Layer에 영향을 미치는 것을 최소화합니다. 더미 데이터 단계에서도 이 분리 원칙을 지키면 추후 실제 데이터 연동 시 구조 변경이 용이합니다.

### 요약

* **Presentation Layer:** Jetpack Compose를 사용하여 사용자 인터페이스를 구성하고, ViewModel(Hilt 주입)을 통해 상태 관리 및 UseCase와 상호작용합니다.
* **Domain Layer:** 핵심 비즈니스 로직을 포함하며, 플랫폼과 데이터 소스로부터 독립적입니다. UseCase, Domain Model, Repository Interface로 구성됩니다.
* **Data Layer:** 데이터 소스와의 상호작용 및 데이터 변환을 담당하며, Domain Layer의 Repository Interface를 구현합니다. Hilt를 사용하여 의존성을 관리합니다. **(DataSource 구현체 내부에서 더미 데이터를 생성하여 초기 개발을 진행할 수 있습니다.)**

이 규칙들을 따르면 각 레이어의 책임이 명확해지고, 코드의 테스트 용이성, 유지보수성, 확장성이 향상될 것입니다. 앱의 기능을 구현할 때 이 규칙들을 일관되게 적용하는 것이 중요합니다.