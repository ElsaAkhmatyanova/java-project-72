@import hexlet.code.dto.UrlListPage
@import hexlet.code.util.NamedRoutes

@param UrlListPage page

@template.layout.main(
title = "Сайты",
content = @`
    <section class="py-5">
        <div class="container">
            <h2 class="mb-4">Сайты</h2>
            <div class="table-responsive" style="max-height: 400px; overflow-y: auto;">
                <table class="table table-bordered align-middle">
                    <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Имя</th>
                        <th>Последняя проверка</th>
                        <th>Код ответа</th>
                    </tr>
                    </thead>
                    <tbody>
                    @for(var url : page.getUrlsWithCheckDtoList())
                        <tr>
                            <td>${url.id()}</td>
                            <td><a href="${NamedRoutes.urlsPath(url.id())}">${url.name()}</a></td>
                            <td>${url.lastCheck()}</td>
                            <td>${url.code()}</td>
                        </tr>
                    @endfor
                    </tbody>
                </table>
            </div>
        </div>
    </section>
`
)