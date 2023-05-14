package com.ute.shopping.util;

import org.apache.poi.hpsf.Decimal;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class MailTemplate {
    public static String head = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "  <head>\n" +
            "    <meta charset=\"utf-8\" />\n" +
            "    <title>Đơn hàng</title>\n" +
            "\n" +
            "    <style>\n" +
            "      .invoice-box {\n" +
            "        max-width: 800px;\n" +
            "        margin: auto;\n" +
            "        padding: 30px;\n" +
            "        border: 1px solid #eee;\n" +
            "        box-shadow: 0 0 10px rgba(0, 0, 0, 0.15);\n" +
            "        font-size: 16px;\n" +
            "        line-height: 24px;\n" +
            "        font-family: \"Helvetica Neue\", \"Helvetica\", Helvetica, Arial, sans-serif;\n" +
            "        color: #555;\n" +
            "      }\n" +
            "\n" +
            "      .invoice-box table {\n" +
            "        width: 100%;\n" +
            "        line-height: inherit;\n" +
            "        text-align: left;\n" +
            "      }\n" +
            "\n" +
            "      .invoice-box table td {\n" +
            "        padding: 5px;\n" +
            "        vertical-align: top;\n" +
            "      }\n" +
            "\n" +
            "      .invoice-box table tr td:nth-child(2) {\n" +
            "        text-align: right;\n" +
            "      }\n" +
            "\n" +
            "      .invoice-box table tr.top table td {\n" +
            "        padding-bottom: 20px;\n" +
            "      }\n" +
            "\n" +
            "      .invoice-box table tr.top table td.title {\n" +
            "        font-size: 45px;\n" +
            "        line-height: 45px;\n" +
            "        color: #333;\n" +
            "      }\n" +
            "\n" +
            "      .invoice-box table tr.information table td {\n" +
            "        padding-bottom: 40px;\n" +
            "      }\n" +
            "\n" +
            "      .invoice-box table tr.heading td {\n" +
            "        background: #eee;\n" +
            "        border-bottom: 1px solid #ddd;\n" +
            "        font-weight: bold;\n" +
            "      }\n" +
            "\n" +
            "      .invoice-box table tr.details td {\n" +
            "        padding-bottom: 8px;\n" +
            "      }\n" +
            "\n" +
            "      .invoice-box table tr.item td {\n" +
            "        border-bottom: 1px solid #eee;\n" +
            "      }\n" +
            "\n" +
            "      .invoice-box table tr.item.last td {\n" +
            "        border-bottom: none;\n" +
            "      }\n" +
            "\n" +
            "      .invoice-box table tr.total td:nth-child(2) {\n" +
            "        border-top: 2px solid #eee;\n" +
            "        font-weight: bold;\n" +
            "      }\n" +
            "\n" +
            "      @media only screen and (max-width: 600px) {\n" +
            "        .invoice-box table tr.top table td {\n" +
            "          width: 100%;\n" +
            "          display: block;\n" +
            "          text-align: center;\n" +
            "        }\n" +
            "\n" +
            "        .invoice-box table tr.information table td {\n" +
            "          width: 100%;\n" +
            "          display: block;\n" +
            "          text-align: center;\n" +
            "        }\n" +
            "      }\n" +
            "\n" +
            "      /** RTL **/\n" +
            "      .invoice-box.rtl {\n" +
            "        direction: rtl;\n" +
            "        font-family: Tahoma, \"Helvetica Neue\", \"Helvetica\", Helvetica, Arial,\n" +
            "          sans-serif;\n" +
            "      }\n" +
            "\n" +
            "      .invoice-box.rtl table {\n" +
            "        text-align: right;\n" +
            "      }\n" +
            "\n" +
            "      .invoice-box.rtl table tr td:nth-child(2) {\n" +
            "        text-align: left;\n" +
            "      }\n" +
            "    </style>\n" +
            "  </head>";

    public static String table(String date) {
        String tableString =
                "<body>\n" +
                        "    <div class=\"invoice-box\">\n" +
                        "      <table cellpadding=\"0\" cellspacing=\"0\">\n" +
                        "        <tr class=\"top\">\n" +
                        "          <td colspan=\"2\">\n" +
                        "            <table>\n" +
                        "              <tr>\n" +
                        "                <td class=\"title\">\n" +
                        "                  <img\n" +
                        "                    src=\"https://raw.githubusercontent.com/TNQuocKhanh/ewebproject/main/src/data/logo.png\"\n" +
                        "                    alt=\"logo-img\"\n" +
                        "                    style=\"width: 100%; max-width: 100px\"\n" +
                        "                  />\n" +
                        "                </td>\n" +
                        "\n" +
                        "                <td>Ngày đặt hàng:" + date + " <br /></td>\n" +
                        "              </tr>\n" +
                        "            </table>\n" +
                        "          </td>\n" +
                        "        </tr>\n" +
                        "\n" +
                        "        <tr class=\"information\">\n" +
                        "          <td colspan=\"2\">\n" +
                        "            <table>\n" +
                        "              <tr>\n" +
                        "                <td>\n" +
                        "                  HDK Shop<br />\n" +
                        "                  01 Võ Văn Ngân<br />\n" +
                        "                  Phường Linh Chiểu, Tp. Thủ Đức\n" +
                        "                </td>\n" +
                        "              </tr>\n" +
                        "            </table>\n" +
                        "          </td>\n" +
                        "        </tr>";
        return tableString;
    }

    public static String customerInfo(String customerName, String phoneNumber, String address) {
        String info = "<tr class=\"heading\">\n" +
                "          <td>Thông tin khách hàng</td>\n" +
                "          <td></td>\n" +
                "        </tr>\n" +
                "        <tr class=\"details\">\n" +
                "          <td>Họ và tên: " + customerName + "</td>\n" +
                "          <td></td>\n" +
                "        </tr>\n" +
                "        <tr class=\"details\">\n" +
                "          <td>SĐT: " + phoneNumber + "</td>\n" +
                "          <td></td>\n" +
                "        </tr>\n" +
                "        <tr class=\"details\">\n" +
                "          <td>Địa chỉ nhận hàng:" + address + "</td>\n" +
                "          <td></td>\n" +
                "        </tr>" +
                "<tr class=\"heading\">\n" +
                "          <td>Sản phẩm</td>\n" +
                "          <td>Giá</td>\n" +
                "        </tr>";
        return info;
    }

    public static String orderInfo(String productName, BigDecimal price) {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        String moneyString = formatter.format(price);
        String order = "<tr class=\"item\">\n" +
                "          <td>" + productName + "</td>\n" +
                "          <td>" + moneyString + "đ </td>\n" +
                "        </tr>";
        return order;
    }

    public static String totalPrice(BigDecimal total) {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        String moneyString = formatter.format(total);
        String price = "<tr class=\"total\">\n" +
                "          <td></td>\n" +
                "\n" +
                "          <td>Tổng cộng: " + moneyString + "đ</td>\n" +
                "        </tr>\n" +
                "      </table>\n" +
                "    </div>\n" +
                "  </body>\n" +
                "</html>";
        return price;
    }
}
