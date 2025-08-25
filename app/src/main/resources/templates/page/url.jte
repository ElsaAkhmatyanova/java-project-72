@import hexlet.code.dto.UrlPage

@param UrlPage page

@template.layout.main(
title = "Сайт",
content = @`
    <section class="py-5">
        <div class="container">
            <h2 class="mb-4">Сайт: ${page.getUrlRecord().name()}</h2>
            <div class="table-responsive" style="max-height: 400px; overflow-y: auto;">
                <table class="table table-bordered align-middle">
                    <tr>
                        <td>ID</td>
                        <td>${page.getUrlRecord().id()}</td>
                    </tr>
                    <tr>
                        <td>Имя</td>
                        <td>${page.getUrlRecord().name()}</td>
                    </tr>
                    <tr>
                        <td>Дата создания</td>
                        <td>${page.getUrlRecord().lastCheck().toString()}</td>
                    </tr>
                </table>
            </div>
        </div>
    </section>
`
)