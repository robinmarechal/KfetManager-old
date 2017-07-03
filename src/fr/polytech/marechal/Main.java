package fr.polytech.marechal;

import fr.polytech.marechal.libs.api.*;
import fr.polytech.marechal.libs.database.query.builders.SelectQueryBuilder;
import fr.polytech.marechal.libs.database.query.builders.enums.Functions;
import fr.polytech.marechal.models.Category;
import fr.polytech.marechal.models.Product;
import fr.polytech.marechal.models.managers.CategoriesManager;
import fr.polytech.marechal.models.managers.ProductsManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Robin
 * @date 10/06/2017
 */
public class Main
{
    public static void main (String[] args)
    {
        //        testBuildWithRelations();
        //        testQueryBuilder();
        //        testOfUrl();
        //        testWithAllRelations();
        //        testLoad();
//        testCreateUpdateDelete();
        
        System.out.println(new CategoriesManager().allWithRelations());
    }

    private static void testCreateUpdateDelete ()
    {
        try
        {
            Category c = new Category();
            c.setName("test test test");

            System.out.println("creation");

            c.saveWithoutRelations();

            System.out.println("update");

            c.setName("test test test test");

            c.saveWithoutRelations();

            System.out.println("deletion");

            c.delete();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static void testLoad ()
    {
        Category c = new CategoriesManager().find(1);

        System.out.println(c);

        c.loadAll(new UrlParametersMap().setLimit(2)
                                        .setOrderBy("id", OrderBy.DESC));

        System.out.println(c);

    }

    private static void testWithAllRelations ()
    {
        ArrayList<Category> categories = new CategoriesManager().all(new UrlParametersMap().withAllRelations());
        for (Category category : categories)
        {
            System.out.println(category);
        }
    }

    private static void testOfUrl ()
    {
        ArrayList<Product> products = new ProductsManager().ofUrl("/categories/1/products", new UrlParametersMap().setRelations(new
                String[]{"subcategory.category"}));

        System.out.println(products.get(0)
                                   .getSubcategory()
                                   .getCategory());
    }

    private static void testQueryBuilder ()
    {
        try
        {
            ApiQuery query = ApiQueryBuilder.forModelManager(new CategoriesManager())
                                            .limit(5, 10)
                                            .orderBy("id")
                                            .with("subcategories", "menus")
                                            .getQuery();

            //            System.out.println(query);
            //
            query.execute()
                 .getJson();
            //
            //            System.out.println("-----------2-----------");

            UrlParametersMap paramMap = new UrlParametersMap().setRelations("menus");

            Category category = new CategoriesManager().find(3, paramMap);

            System.out.println(category);

            category.getMenus()
                    .getPivotList()
                    .forEach(categoryMenu -> System.out.println(categoryMenu));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static void apiTests ()
    {
        try
        {
            examplePostRequest();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        try
        {
            examplePutRequest();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        try
        {
            exampleGetRequest();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        try
        {
            exampleDeleteRequest();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        try
        {
            getAllCategories();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

    }

    private static void examplePostRequest () throws Exception
    {
        long l = LocalDateTime.now()
                              .toEpochSecond(ZoneOffset.UTC);

        HashMap<String, Object> data = new HashMap<>();
        data.put("name", "cateTestApi" + l);

        ApiResponse resp = Api.post("api://kfetmanager.api/api/categories", data);

        System.out.println(resp.getCode());
        System.out.println(resp.getJson()
                               .toJSONString());
    }

    private static void exampleDeleteRequest () throws IOException, ParseException
    {
        ApiResponse resp = Api.delete("api://kfetmanager.api/api/categories/7");

        System.out.println(resp.getCode());
        System.out.println(resp.getJson()
                               .toJSONString());
    }

    private static void exampleGetRequest () throws IOException, ParseException
    {
        ApiResponse resp = Api.get("api://kfetmanager.api/api/categories/1");

        System.out.println(resp.getCode());
        System.out.println(resp.getJson()
                               .toJSONString());
    }

    private static void getAllCategories () throws IOException, ParseException
    {
        ApiResponse resp = Api.get("api://kfetmanager.api/api/categories");

        System.out.println(resp.getCode());
        JSONArray arr = resp.getJson();

        arr.forEach(o -> System.out.println(((JSONObject) o).get("id")));
    }

    private static void examplePutRequest () throws IOException, ParseException
    {
        long l = LocalDateTime.now()
                              .toEpochSecond(ZoneOffset.UTC);

        HashMap<String, Object> data = new HashMap<>();
        data.put("name", "PutTest" + l);

        ApiResponse resp = Api.put("api://kfetmanager.api/api/categories/2", data);

        System.out.println(resp.getCode());
        System.out.println(resp.getJson()
                               .toJSONString());
    }

    private static void exampleFactory ()
    {
        JSONObject json = new JSONObject();
        json.put("id", 5);
        json.put("name", "abc");
        json.put("subcategory_id", 8);
        json.put("price", 3.20);
        json.put("stock", 15);
        json.put("description", "teeeest");

        Product p = null;

        try
        {
            p = new ProductsManager().buildFromJson(json);
        }
        catch (ReflectiveOperationException e)
        {
            e.printStackTrace();
        }

        System.out.println(p);

        System.out.println(json);
    }

    private static void exampleCount ()
    {
        String q = SelectQueryBuilder.select("table")
                                     .count()
                                     .where("pk", "=", "pkValue")
                                     .buildQuery()
                                     .toString();

        System.out.println(q);
    }

    private static void exampleSelect ()
    {
        String q = SelectQueryBuilder.select("table")
                                     .where("name", "=", "name1")
                                     .orWhere("name", "=", "name2")
                                     .innerJoin("Table2", "id1", "=", "table_id")
                                     .groupBy("group")
                                     .having(Functions.COUNT, "f1", ">", 5)
                                     .orderBy("name", fr.polytech.marechal.libs.database.query.builders.enums.OrderBy.ASC)
                                     .limit(5)
                                     .offset(10)
                                     .buildQuery()
                                     .toString();

        System.out.println(q);
    }
}
