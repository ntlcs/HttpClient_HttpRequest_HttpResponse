import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

public class GoogleBooksSearch {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Digite o título do livro que deseja buscar:");
        String bookTitle = scanner.nextLine();

        try {
            // Formatar a URL da API do Google Books
            String url = "https://www.googleapis.com/books/v1/volumes?q=intitle:" + bookTitle.replace(" ", "+");

            // Criar um cliente HTTP
            HttpClient client = HttpClient.newHttpClient();

            // Criar uma solicitação HTTP
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .GET()
                    .build();

            // Enviar a solicitação e obter a resposta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Verificar se a resposta foi bem-sucedida
            if (response.statusCode() == 200) {
                // Parsear o corpo da resposta JSON
                JSONObject jsonResponse = new JSONObject(response.body());
                JSONArray items = jsonResponse.getJSONArray("items");

                if (items.length() > 0) {
                    // Obter o primeiro livro retornado
                    JSONObject book = items.getJSONObject(0).getJSONObject("volumeInfo");

                    // Exibir informações sobre o livro
                    String title = book.optString("title", "Título não disponível");
                    String authors = book.has("authors") ? String.join(", ", book.getJSONArray("authors").toList().toArray(new String[0])) : "Autores não disponíveis";
                    String description = book.optString("description", "Descrição não disponível");
                    String publisher = book.optString("publisher", "Editora não disponível");
                    String publishedDate = book.optString("publishedDate", "Data de publicação não disponível");

                    System.out.println("Título: " + title);
                    System.out.println("Autores: " + authors);
                    System.out.println("Descrição: " + description);
                    System.out.println("Editora: " + publisher);
                    System.out.println("Data de publicação: " + publishedDate);
                } else {
                    System.out.println("Nenhum livro encontrado com esse título.");
                }
            } else {
                System.out.println("Erro na consulta à API: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Ocorreu um erro ao tentar buscar informações sobre o livro.");
        }

        scanner.close();
    }
}
