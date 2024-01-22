# MyLogBook

For the reviewer: 

1. The app follows clean architecture approach with MVVM pattern + Jetpack Compose. 
2. For the persistence I decided to go with Room as it's the recommended way by Google.
3. I decided to use BigDecimal for storing the entries as it's more accurate than Double, and it's
important not to lose any precision whenever we're operating on the data. Might be overkill though in 
hindsight.
4. Jetpack Compose for the UI as it's the future of Android UI development :) 
5. For the DI I used Hilt since we have a simple app here and it's more than enough.
6. There is a separation between the domain, data and the ui layers according to the clean architecture.

List of potential improvements & known issues: 

- Uniting the 3 flows into a single uiState flow (didn't have the time).
- Passing some domain data to the list in the UI while it should've been formatted beforehand.
- Should implement use cases for the formatting options and validation.
- Separating domain, data and UI into their own modules to enforce the Clean Architecture principles
 even more.
- The list with value right now recomposes each time which is suboptimal, should use keys to determine 
if each item needs recomposition individually.
- UI tests + repository tests not implemented. 
- Have not covered all domain states properly in the ViewModel due to time constraints.

Thank you!