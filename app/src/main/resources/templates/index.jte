@import hexlet.code.dto.MainPage
@import hexlet.code.util.NamedRoutes

@param MainPage page

@template.layout.main(
title = "Анализатор страниц",
page = page,
content = @`
    <section class="py-5 text-center">
        <div class="container">
            <h1 class="display-4">Анализатор страниц</h1>
            <p class="lead">Проверяйте сайты на SEO-пригодность бесплатно</p>

            <form action="${NamedRoutes.urlsPath()}" method="post" class="row g-2 justify-content-center">
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
`
)