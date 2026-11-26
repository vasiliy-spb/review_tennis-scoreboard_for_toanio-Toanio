# Роадмап рефакторинга по файлам

Это упорядоченный список файлов, которые следует исправлять в соответствии с замечаниями в комментариях. Рекомендую двигаться последовательно.

Файлы, не указанные в списке, можно исправлять в любом порядке.

### Шаг 1: Entity и слой доступа к данным

- `/match/Match.java`
- `/player/Player.java`
- `/player/PlayerRepository.java`
- `/match/MatchRepository.java`

### Шаг 2: Доменные модели

- `/match/ActivePlayer.java`
- `/match/ActiveMatch.java`

### Шаг 3: Сервисный слой

- `/match/MatchService.java`
- `/player/PlayerService.java`

### Шаг 4: Контроллер и DTO

- `/player/PlayerController.java`
- `/match/MatchController.java`
- `/player/dto/*`
- `/match/dto/*`

### Шаг 5: Конфигурация, мапперы, валидаторы, обработка исключений

- `/exception/GlobalExceptionHandler.java`
- `/exception/*`
- `/player/PlayerMapper.java`
- `/match/MatchMapper.java`
- `/match/ActivePlayerMapper.java`

### Шаг 6: Тесты

- `/test/java/com/example/tennis/scoreboard/match/ActiveMatchTest.java`
