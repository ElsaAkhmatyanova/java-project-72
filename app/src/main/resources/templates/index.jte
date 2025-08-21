<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Анализатор страниц</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-LN+7fdVzj6u52u30Kp6M/trliBMCMKTyK833zpbD+pXdCLuTusPj697FH4R/5mcr"
          crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-ndDqU0Gzau9qJ1lfW4pNLlhNTkCfHzAVBReH9diLvGRem5+R9g2FzA8ZGN954O5Q"
            crossorigin="anonymous"></script>
</head>

<body class="d-flex flex-column min-vh-100">

<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
        <a class="navbar-brand" href="/">Анализатор страниц</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                data-bs-target="#navbarNav" aria-controls="navbarNav"
                aria-expanded="false" aria-label="Меню">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav">
                <li class="nav-item"><a class="nav-link" href="/">Главная</a></li>
                <li class="nav-item"><a class="nav-link" href="/urls">Сайты</a></li>
            </ul>
        </div>
    </div>
</nav>

<main class="flex-grow-1">
    <section class="py-5 bg-light text-center">
        <div class="container">
            <h1 class="display-4">Анализатор страниц</h1>
            <p class="lead">Проверяйте сайты на SEO-пригодность бесплатно</p>

            <form action="/urls" method="post" class="row g-2 justify-content-center">
                <div class="col-md-6">
                    <input id="url-input" type="url" name="url" class="form-control"
                           placeholder="Введите ссылку" required>
                </div>
                <div class="col-auto">
                    <button type="submit" class="btn btn-primary">Проверить</button>
                </div>
            </form>

            <p class="mt-2 text-muted">Пример: https://www.example.com</p>
        </div>
    </section>
</main>

<footer class="mt-auto py-3 bg-light border-top">
    <div class="container text-center">
        created by <a href="https://github.com/ElsaAkhmatyanova" target="_blank">Elza Akhmatyanova (Github)</a>
    </div>
</footer>

</body>
</html>