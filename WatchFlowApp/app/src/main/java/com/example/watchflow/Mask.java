package com.example.watchflow;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class Mask {

    public static String unmask(String s) {
        return s.replaceAll("[.]", "").replaceAll("[-]", "")
                .replaceAll("[/]", "").replaceAll("[(]", "")
                .replaceAll("[)]", "");
    }

    public static String mask(String text, String mask, String controlCharacters) {

        if (text == null || text.length() < 1) {
            return "";
        }

        int nCount = 0;
        StringBuilder maskedString = new StringBuilder();

        for (int i = 0; i <= mask.length(); i++) {
            try {
                char character = ' ';
                character = mask.charAt(i);

                boolean bolMask = controlCharacters.contains(character + "");

                if (bolMask) {
                    maskedString.append(character);
                } else {
                    maskedString.append(text.charAt(nCount));
                    nCount++;
                }
            } catch (StringIndexOutOfBoundsException e) {
                return maskedString.toString();
            }
        }
        return maskedString.toString();
    }

    public static TextWatcher insert(final String mask, final EditText ediTxt) {
        return new TextWatcher() {
            boolean isUpdating;
            String old = "";

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = Mask.unmask(s.toString());
                String mascara = "";
                if (isUpdating) {
                    old = str;
                    isUpdating = false;
                    return;
                }
                int i = 0;
                for (char m : mask.toCharArray()) {
                    if (m != '#' && str.length() > old.length()) {
                        mascara += m;
                        continue;
                    }
                    try {
                        mascara += str.charAt(i);
                    } catch (Exception e) {
                        break;
                    }
                    i++;
                }
                isUpdating = true;
                ediTxt.setText(mascara);
                ediTxt.setSelection(mascara.length());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        };
    }

}