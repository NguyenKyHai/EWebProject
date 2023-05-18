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
                        "                <td>Ngày đặt hàng: " + date + " <br /></td>\n" +
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
                "          <td>Địa chỉ nhận hàng: " + address + "</td>\n" +
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

    public static String verifyCode(String code) {
        String verifyString = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "  <head>\n" +
                "    <meta charset=\"utf-8\" />\n" +
                "    <meta http-equiv=\"x-ua-compatible\" content=\"ie=edge\" />\n" +
                "    <title>Email Confirmation</title>\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\" />\n" +
                "    <style type=\"text/css\">\n" +
                "      body,\n" +
                "      table,\n" +
                "      td,\n" +
                "      table,\n" +
                "      div[style*=\"margin: 16px 0;\"] {\n" +
                "        margin: 0 !important;\n" +
                "      }\n" +
                "      body {\n" +
                "        width: 100% !important;\n" +
                "        height: 100% !important;\n" +
                "        padding: 0 !important;\n" +
                "        margin: 0 !important;\n" +
                "      }\n" +
                "      table {\n" +
                "        border-collapse: collapse !important;\n" +
                "      }\n" +
                "      a {\n" +
                "        color: #1a82e2;\n" +
                "      }\n" +
                "      img {\n" +
                "        height: auto;\n" +
                "        line-height: 100%;\n" +
                "        text-decoration: none;\n" +
                "        border: 0;\n" +
                "        outline: none;\n" +
                "      }\n" +
                "    </style>\n" +
                "  </head>\n" +
                "  <body style=\"background-color: #e9ecef\">\n" +
                "    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                "      <tr>\n" +
                "        <td align=\"center\" bgcolor=\"#e9ecef\">\n" +
                "          <table\n" +
                "            border=\"0\"\n" +
                "            cellpadding=\"0\"\n" +
                "            cellspacing=\"0\"\n" +
                "            width=\"100%\"\n" +
                "            style=\"max-width: 600px\"\n" +
                "          >\n" +
                "            <tr>\n" +
                "              <td align=\"center\" valign=\"top\" style=\"padding: 36px 24px\">\n" +
                "                <a\n" +
                "                  href=\"https://www.blogdesire.com\"\n" +
                "                  target=\"_blank\"\n" +
                "                  style=\"display: inline-block\"\n" +
                "                >\n" +
                "                  <img\n" +
                "                    src=\"https://raw.githubusercontent.com/TNQuocKhanh/ewebproject/main/src/data/logo.png\"\n" +
                "                    alt=\"logo-img\"\n" +
                "                    style=\"width: 100%; max-width: 100px\"\n" +
                "                  />\n" +
                "                </a>\n" +
                "              </td>\n" +
                "            </tr>\n" +
                "          </table>\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "      <tr>\n" +
                "        <td align=\"center\" bgcolor=\"#e9ecef\">\n" +
                "          <table\n" +
                "            border=\"0\"\n" +
                "            cellpadding=\"0\"\n" +
                "            cellspacing=\"0\"\n" +
                "            width=\"100%\"\n" +
                "            style=\"max-width: 600px\"\n" +
                "          >\n" +
                "            <tr>\n" +
                "              <td\n" +
                "                align=\"left\"\n" +
                "                bgcolor=\"#ffffff\"\n" +
                "                style=\"\n" +
                "                  padding: 36px 24px 0;\n" +
                "                  font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif;\n" +
                "                  border-top: 5px solid #f5c24c;\n" +
                "                \"\n" +
                "              >\n" +
                "                <h2\n" +
                "                  style=\"\n" +
                "                    margin: 0;\n" +
                "                    text-align: center;\n" +
                "                    font-size: 32px;\n" +
                "                    font-weight: 600;\n" +
                "                    letter-spacing: -1px;\n" +
                "                    line-height: 48px;\n" +
                "                  \"\n" +
                "                >\n" +
                "                  Xác thực địa chỉ email\n" +
                "                </h2>\n" +
                "              </td>\n" +
                "            </tr>\n" +
                "          </table>\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "      <tr>\n" +
                "        <td align=\"center\" bgcolor=\"#e9ecef\">\n" +
                "          <table\n" +
                "            border=\"0\"\n" +
                "            cellpadding=\"0\"\n" +
                "            cellspacing=\"0\"\n" +
                "            width=\"100%\"\n" +
                "            style=\"max-width: 600px\"\n" +
                "          >\n" +
                "            <tr>\n" +
                "              <td\n" +
                "                align=\"left\"\n" +
                "                bgcolor=\"#ffffff\"\n" +
                "                style=\"\n" +
                "                  padding: 24px;\n" +
                "                  font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif;\n" +
                "                  font-size: 16px;\n" +
                "                  line-height: 24px;\n" +
                "                \"\n" +
                "              >\n" +
                "                <p style=\"margin: 0\">Mã xác thực của Quý khách là:</p>\n" +
                "              </td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "              <td align=\"left\" bgcolor=\"#ffffff\">\n" +
                "                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                "                  <tr>\n" +
                "                    <td align=\"center\" bgcolor=\"#ffffff\" style=\"padding: 12px\">\n" +
                "                      <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                "                        <tr>\n" +
                "                          <td\n" +
                "                            align=\"center\"\n" +
                "                            bgcolor=\"#f5c24c\"\n" +
                "                            style=\"border-radius: 6px\"\n" +
                "                          >\n" +
                "                            <a\n" +
                "                              style=\"\n" +
                "                                display: inline-block;\n" +
                "                                padding: 16px 36px;\n" +
                "                                font-family: 'Source Sans Pro', Helvetica, Arial,\n" +
                "                                  sans-serif;\n" +
                "                                font-size: 20px;\n" +
                "                                color: #ffffff;\n" +
                "                                text-decoration: none;\n" +
                "                                border-radius: 6px;\n" +
                "                              \"\n" +
                "                              >" + code + "</a\n" +
                "                            >\n" +
                "                          </td>\n" +
                "                        </tr>\n" +
                "                      </table>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </table>\n" +
                "              </td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "              <td\n" +
                "                align=\"left\"\n" +
                "                bgcolor=\"#ffffff\"\n" +
                "                style=\"\n" +
                "                  padding: 24px;\n" +
                "                  font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif;\n" +
                "                  font-size: 16px;\n" +
                "                  line-height: 24px;\n" +
                "                \"\n" +
                "              >\n" +
                "                <p style=\"margin: 0; text-align: center\">\n" +
                "                  Quý khách vui lòng bỏ qua email này nếu đây không phải là\n" +
                "                  email của Quý khách.\n" +
                "                </p>\n" +
                "                <br />\n" +
                "                <p style=\"margin: 0; text-align: center\">\n" +
                "                  Trân trọng cảm ơn Quý khách đã tin tưởng sử dụng dịch vụ!\n" +
                "                </p>\n" +
                "              </td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "              <td\n" +
                "                align=\"left\"\n" +
                "                bgcolor=\"#ffffff\"\n" +
                "                style=\"\n" +
                "                  padding: 24px;\n" +
                "                  font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif;\n" +
                "                  font-size: 16px;\n" +
                "                  line-height: 24px;\n" +
                "                  border-bottom: 3px solid #d4dadf;\n" +
                "                \"\n" +
                "              >\n" +
                "                <br />\n" +
                "                <p style=\"margin: 0\">\n" +
                "                  Trân trọng,<br />\n" +
                "                  HDK Shop\n" +
                "                </p>\n" +
                "                <hr />\n" +
                "                <b>Điện thoại:</b> 0966 230 556<br />\n" +
                "                <b>Email:</b> leafnote2022@gmail.com<br />\n" +
                "                <b>Địa chỉ:</b> 01 Võ Văn Ngân, P. Linh Chiểu, Tp. Thủ Đức\n" +
                "              </td>\n" +
                "            </tr>\n" +
                "          </table>\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "      <tr>\n" +
                "        <td align=\"center\" bgcolor=\"#e9ecef\" style=\"padding: 24px\">\n" +
                "          <table\n" +
                "            border=\"0\"\n" +
                "            cellpadding=\"0\"\n" +
                "            cellspacing=\"0\"\n" +
                "            width=\"100%\"\n" +
                "            style=\"max-width: 600px\"\n" +
                "          >\n" +
                "            <tr>\n" +
                "              <td\n" +
                "                align=\"center\"\n" +
                "                bgcolor=\"#e9ecef\"\n" +
                "                style=\"\n" +
                "                  padding: 12px 24px;\n" +
                "                  font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif;\n" +
                "                  font-size: 14px;\n" +
                "                  line-height: 20px;\n" +
                "                  color: #666;\n" +
                "                \"\n" +
                "              >\n" +
                "                <p style=\"margin: 0\">\n" +
                "                  Bạn nhận được email này vì chúng tôi đã nhận được một yêu cầu\n" +
                "                  từ tài khoản của bạn. Nếu đây không phải là yêu cầu từ phía\n" +
                "                  bạn, vui lòng xoá email này.\n" +
                "                </p>\n" +
                "              </td>\n" +
                "            </tr>\n" +
                "          </table>\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "    </table>\n" +
                "  </body>\n" +
                "</html>";
        return verifyString;
    }
}
