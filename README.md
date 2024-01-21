# MyLogBook


1. The app follows clean architecture approach with MVVM pattern + Jetpack Compose. 
2. For the persistent I decided to go with Room as it's the recommended way by Google.
3. I decided to use BigDecimal for storing the entries as it's more accurate than Double, and it's
important not to lose any precision whenever we're operating on the data. 
4. Jetpack Compose for the UI as it's the future of Android UI development :) 
5. For the DI I used Hilt since we have a simple app here and it's more than enough
6. There is a separation between the domain, data and the ui layers according to the clean architecture.
