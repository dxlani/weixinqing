package me.mymilkbottles.weixinqing.util;

import org.apache.commons.lang3.CharUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/06/23 14:51.
 */
public class SensitiveWordFilterUtil {

    private static WordTree wordTree = new WordTree();

    private static final String SENSITIVE_WORD_REPLACE = "***";

    private static class WordTree {

        private Map<Character, WordTree> tree = new HashMap<Character, WordTree>();

        private boolean isWordEndFlag;

        public WordTree() {
        }

        public WordTree(boolean isWordEndFlag) {
            this.isWordEndFlag = isWordEndFlag;
        }

        public boolean getIsWordEndFlag() {
            return isWordEndFlag;
        }

        public void set(Character ch, WordTree wordTree) {
            tree.put(ch, wordTree);
        }

        public WordTree get(Character ch) {
            return tree.get(ch);
        }
    }

    public static void addSensitiveWord(String sensitiveWord) {

        WordTree wordTreeNode = wordTree;

        for (int i = 0, len = sensitiveWord.length(); i < len; ++i) {

            Character nowCharacter = sensitiveWord.charAt(i);

            WordTree tempWordTreeNode = new WordTree(i == len - 1);

            wordTreeNode.set(nowCharacter, tempWordTreeNode);

            wordTreeNode = tempWordTreeNode;
        }
    }

    public static String filter(String filterText) {

        StringBuilder afterFilterText = new StringBuilder();

        int len = filterText.length();

        int nowPosition = 0, testPosition, filterPosition;

        while (nowPosition < len) {
            WordTree wordTreeNode = wordTree;

            Character nowCharacter = filterText.charAt(nowPosition);

            WordTree tempWordTreeNode = wordTreeNode.get(nowCharacter);

            if (tempWordTreeNode == null) {
                afterFilterText.append(nowCharacter);
                ++nowPosition;
            } else {
                testPosition = nowPosition;
                filterPosition = -1;
                while (tempWordTreeNode != null) {
                    if (tempWordTreeNode.getIsWordEndFlag()) {
                        filterPosition = testPosition;
                        break;
                    } else {
                        while ((++testPosition) < len) {
                            nowCharacter = filterText.charAt(testPosition);
                            if (CharUtils.isAsciiAlphanumeric(nowCharacter) || (nowCharacter >= 0x4e00 && nowCharacter <= 0x9fbb)) {
                                break;
                            }
                        }
                        tempWordTreeNode = tempWordTreeNode.get(nowCharacter);
                    }
                }
                if (filterPosition != -1) {
                    afterFilterText.append(SENSITIVE_WORD_REPLACE);
                    nowPosition = filterPosition + 1;
                } else {
                    afterFilterText.append(filterText.charAt(nowPosition));
                    ++nowPosition;
                }
            }
        }

        return afterFilterText.toString();
    }

//        public static void main(String[] args) {
//            SensitiveWordFilter sensitiveWordFilter = new SensitiveWordFilter();
//
//            String[] sensitiveWords =
//                    new String[]{"色情", "暴力", "赌博", "上网", "通宵", "sb", "傻逼", "智障", "你妈"};
//
//            for (String sensitiveWord : sensitiveWords) {
//                sensitiveWordFilter.addSensitiveWord(sensitiveWord);
//            }
//
//            String needFilterText = "色情？色情是什么？大家都说我是一个色            情狂（其实我不是色┬＿┬情狂），但是我自己并不这样认为，因为我平时喜欢打篮球，但是有人" +
//                    "说我打篮球的样子像一个智障一样，我只能对他说：我操你妈，你这个傻逼东西敢骂我，" +
//                    "等你下次上网的时候我把你的单车轮胎气都放光，让你回不去只能在网吧通宵，实在不行我就使用暴= 力。" +
//                    "打死你这个s*b东西，骂我？那你就是在拿自己的生命做赌博！！！！（我不色&情的）";
//
//            String normalText = sensitiveWordFilter.filter(needFilterText);
//
//            System.out.println(normalText);
//
//            boolean isFilterSuccess = true;
//
//            for (String sensitiveWord : sensitiveWords) {
//                if (normalText.contains(sensitiveWord)) {
//                    isFilterSuccess = false;
//                    break;
//                }
//            }
//
//            System.out.println("sensitive word filter " + (isFilterSuccess == true ? "success" : "failure"));
//        }


}
