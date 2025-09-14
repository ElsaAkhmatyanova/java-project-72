@import hexlet.code.dto.page.UrlPage
@import hexlet.code.util.NamedRoutes

@param UrlPage page

@template.layout.main(
title = "Сайт",
page = page,
content = @`
    <section class="py-5">
        <div class="container">
            <h2 class="mb-4">Сайт: ${page.getUrlInfoDto().name()}</h2>
            <div class="table-responsive" style="max-height: 400px; overflow-y: auto;">
                <table class="table table-bordered align-middle">
                    <tr>
                        <td>ID</td>
                        <td>${page.getUrlInfoDto().id()}</td>
                    </tr>
                    <tr>
                        <td>Имя</td>
                        <td>${page.getUrlInfoDto().name()}</td>
                    </tr>
                    <tr>
                        <td>Дата создания</td>
                        <td>${page.getUrlInfoDto().createdAt()}</td>
                    </tr>
                </table>
            </div>
            <h2 class="mb-3">Проверки</h2>
            <form method="post" action="${NamedRoutes.urlChecksPath(page.getUrlInfoDto().id())}" class="mb-3">
                <button type="submit" class="btn btn-primary">Запустить проверку</button>
            </form>
            <div class="table-responsive" style="max-height: 400px; overflow-y: auto;">
                <table class="table table-bordered align-middle">
                    <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Код ответа</th>
                        <th>title</th>
                        <th>h1</th>
                        <th>description</th>
                        <th>Дата проверки</th>
                    </tr>
                    </thead>
                    <tbody>
                    @for(var urlCheck : page.getUrlCheckInfoDtoList())
                        <tr>
                            <td>${urlCheck.id()}</td>
                            <td>${urlCheck.code()}</td>
                            <td>${urlCheck.title()}</td>
                            <td>${urlCheck.h1()}</td>
                            <td>${urlCheck.description()}</td>
                            <td>${urlCheck.createdAt()}</td>
                        </tr>
                    @endfor
                    </tbody>
                </table>
            </div>
        </div>
    </section>
`
)