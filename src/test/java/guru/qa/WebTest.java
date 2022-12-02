package guru.qa;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class WebTest {

    @BeforeEach
    void setUp() {
      open("https://www.wildberries.ru");
      $x("(//div[@class='banners-zones'])[1]").shouldBe(visible, Duration.ofSeconds(10));
        Configuration.holdBrowserOpen = false;
        Configuration.browserSize="3920x3080";
    }

    @CsvFileSource(resources = "source.csv")
    @ParameterizedTest(name="Проверка поиска {0} в магазине")
    @Tag("BLOCKER")
    void searchTest(String searchingItem){
        $("#searchInput").setValue(searchingItem).pressEnter();
        $x("//div[@class='product-card j-card-item j-good-for-listing-event'][1]").shouldBe(visible, Duration.ofSeconds(10));
    }

    @ValueSource(strings={"Клевые носки"})
    @DisplayName("Проверка добавления товара в корзину")
    @ParameterizedTest
    @Tag("BLOCKER")
    void addToBasketTest(String arg){
        $("#searchInput").setValue(arg).pressEnter();
        $x("//div[@class='product-card j-card-item j-good-for-listing-event'][1]").shouldBe(visible).click();
        $x("(//span[@class='hide-mobile'][contains(text(),'Добавить в корзину')])[2]").shouldBe(visible).click();
        $x("(//p[@class='action-notification show'])[1]").shouldBe(visible);
        $x("(//span[@class='navbar-pc__icon navbar-pc__icon--basket'])[1]").shouldBe(visible).click();
        $x("(//h1[contains(text(),'Корзина')])[1]").shouldBe(visible);

    }


    static Stream<Arguments> checkButtons(){
        return Stream.of(
                Arguments.of("Навигация по сайту", List.of("Женщинам\n" +
                        "Обувь\n" +
                        "Детям\n" +
                        "Мужчинам\n" +
                        "Дом\n" +
                        "Новый год\n" +
                        "Красота\n" +
                        "Аксессуары\n" +
                        "Электроника\n" +
                        "Игрушки\n" +
                        "Мебель\n" +
                        "Товары для взрослых\n" +
                        "Продукты\n" +
                        "Бытовая техника\n" +
                        "Зоотовары\n" +
                        "Спорт\n" +
                        "Автотовары\n" +
                        "Книги\n" +
                        "Premium\n" +
                        "Ювелирные изделия\n" +
                        "Для ремонта\n" +
                        "Сад и дача\n" +
                        "Здоровье\n" +
                        "Цифровые товары\n" +
                        "Канцтовары\n" +
                        "Акции\n" +
                        "Сделано в Москве\n" +
                        "Авиабилеты\n" +
                        "Бренды\n" +
                        "Видеообзоры"))
        );
    }

    @MethodSource
    @ParameterizedTest(name="Проверка списка кнопок в {0} из выпадающего меню")
    @Tag("BLOCKER")
    void checkButtons(String navigation, List<String> buttons){
        $("[type=button][aria-label='"+navigation+"']").shouldBe(visible).click();
        $$(".menu-burger__main").filter(visible).shouldHave(CollectionCondition.texts(buttons));
    }
}