#! /bin/bash
printf "fail_char_1.scm\n" > test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/fail_char_1.scm >> test_output.txt
printf "fail_char_2.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/fail_char_2.scm >> test_output.txt
printf "fail_char_3.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/fail_char_3.scm >> test_output.txt
printf "fail_char_4.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/fail_char_4.scm >> test_output.txt
printf "fail_identifier_1.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/fail_identifier_1.scm >> test_output.txt
printf "fail_identifier_2.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/fail_identifier_2.scm >> test_output.txt
printf "fail_identifier_3.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/fail_identifier_3.scm >> test_output.txt
printf "fail_identifier_4.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/fail_identifier_4.scm >> test_output.txt
printf "fail_identifier_5.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/fail_identifier_5.scm >> test_output.txt
printf "fail_identifier_6.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/fail_identifier_6.scm >> test_output.txt
printf "fail_identifier_7.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/fail_identifier_7.scm >> test_output.txt
printf "fail_num_1.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/fail_num_1.scm >> test_output.txt
printf "fail_num_2.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/fail_num_2.scm >> test_output.txt
printf "fail_num_3.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/fail_num_3.scm >> test_output.txt
printf "fail_num_4.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/fail_num_4.scm >> test_output.txt
printf "fail_string_1.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/fail_string_1.scm >> test_output.txt
printf "fail_string_2.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/fail_string_2.scm >> test_output.txt
printf "fail_string_3.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/fail_string_3.scm >> test_output.txt
printf "fail_token_1.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/fail_token_1.scm >> test_output.txt
printf "fail_token_2.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/fail_token_2.scm >> test_output.txt
printf "fail_token_3.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/fail_token_3.scm >> test_output.txt
printf "fail_token_4.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/fail_token_4.scm >> test_output.txt
printf "fail_token_5.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/fail_token_5.scm >> test_output.txt
printf "fail_token_6.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/fail_token_6.scm >> test_output.txt
printf "fail_token_7.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/fail_token_7.scm >> test_output.txt
printf "pass_bool_false.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_bool_false.scm >> test_output.txt
printf "pass_bool_true.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_bool_true.scm >> test_output.txt
printf "pass_char_a.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_char_a.scm >> test_output.txt
printf "pass_char_backslash.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_char_backslash.scm >> test_output.txt
printf "pass_char_lparen.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_char_lparen.scm >> test_output.txt
printf "pass_char_newline.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_char_newline.scm >> test_output.txt
printf "pass_char_pound.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_char_pound.scm >> test_output.txt
printf "pass_char_quote.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_char_quote.scm >> test_output.txt
printf "pass_char_rparen.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_char_rparen.scm >> test_output.txt
printf "pass_char_space.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_char_space.scm >> test_output.txt
printf "pass_char_tab.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_char_tab.scm >> test_output.txt
printf "pass_char_tick.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_char_tick.scm >> test_output.txt
printf "pass_comment.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_comment.scm >> test_output.txt
printf "pass_empty.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_empty.scm >> test_output.txt
printf "pass_float_minus.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_float_minus.scm >> test_output.txt
printf "pass_float_plus.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_float_plus.scm >> test_output.txt
printf "pass_float.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_float.scm >> test_output.txt
printf "pass_id_amp.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_id_amp.scm >> test_output.txt
printf "pass_id_carat.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_id_carat.scm >> test_output.txt
printf "pass_id_charnum.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_id_charnum.scm >> test_output.txt
printf "pass_id_colon.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_id_colon.scm >> test_output.txt
printf "pass_id_complex.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_id_complex.scm >> test_output.txt
printf "pass_id_div.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_id_div.scm >> test_output.txt
printf "pass_id_dollar.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_id_dollar.scm >> test_output.txt
printf "pass_id_excl.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_id_excl.scm >> test_output.txt
printf "pass_id_gt.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_id_gt.scm >> test_output.txt
printf "pass_id_lt.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_id_lt.scm >> test_output.txt
printf "pass_id_percent.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_id_percent.scm >> test_output.txt
printf "pass_id_question.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_id_question.scm >> test_output.txt
printf "pass_id_star.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_id_star.scm >> test_output.txt
printf "pass_id_tilde.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_id_tilde.scm >> test_output.txt
printf "pass_id_underscore.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_id_underscore.scm >> test_output.txt
printf "pass_int_minus.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_int_minus.scm >> test_output.txt
printf "pass_int_plus.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_int_plus.scm >> test_output.txt
printf "pass_int.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_int.scm >> test_output.txt
printf "pass_kw_and.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_kw_and.scm >> test_output.txt
printf "pass_kw_begin.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_kw_begin.scm >> test_output.txt
printf "pass_kw_cond.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_kw_cond.scm >> test_output.txt
printf "pass_kw_define.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_kw_define.scm >> test_output.txt
printf "pass_kw_if.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_kw_if.scm >> test_output.txt
printf "pass_kw_lambda.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_kw_lambda.scm >> test_output.txt
printf "pass_kw_lparen.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_kw_lparen.scm >> test_output.txt
printf "pass_kw_or.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_kw_or.scm >> test_output.txt
printf "pass_kw_quote.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_kw_quote.scm >> test_output.txt
printf "pass_kw_rparen.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_kw_rparen.scm >> test_output.txt
printf "pass_kw_set.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_kw_set.scm >> test_output.txt
printf "pass_kw_vec.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_kw_vec.scm >> test_output.txt
printf "pass_line_col.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_line_col.scm >> test_output.txt
printf "pass_quote_float.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_quote_float.scm >> test_output.txt
printf "pass_quote_int.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_quote_int.scm >> test_output.txt
printf "pass_quote_list_empty.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_quote_list_empty.scm >> test_output.txt
printf "pass_quote_list_nonempty.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_quote_list_nonempty.scm >> test_output.txt
printf "pass_quote_symbol_long.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_quote_symbol_long.scm >> test_output.txt
printf "pass_quote_symbol_short.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_quote_symbol_short.scm >> test_output.txt
printf "pass_quote_vec_nonempty.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_quote_vec_nonempty.scm >> test_output.txt
printf "pass_string_escaped.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_string_escaped.scm >> test_output.txt
printf "pass_string.scm\n" >> test_output.txt
java -jar app/build/libs/app.jar -scan ../../scan_tests/pass_string.scm >> test_output.txt
