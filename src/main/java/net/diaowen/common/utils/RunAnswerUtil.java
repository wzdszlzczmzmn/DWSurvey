package net.diaowen.common.utils;

import net.diaowen.dwsurvey.entity.Question;
import net.diaowen.dwsurvey.service.SurveyAnswerManager;
import net.diaowen.dwsurvey.service.impl.SurveyAnswerManagerImpl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * RunAnswerUtil提供了用于获取新问题Map的工具方法。
 * 这个类允许用户传入问题列表和调查回答ID，并返回一个新的问题映射，其中包含了所有问题的索引和对应的Question对象。
 * 它通过多线程并行处理每个问题，最终构建并返回一个完整的问题映射。
 */
public class RunAnswerUtil {

    /**
     * 返回新question Map
     * @param questions 问题列表
     * @param surveyAnswerId 调查回答ID
     * @return 新的question Map
     */
    public Map<Integer,Question> getQuestionMap(List<Question> questions,String surveyAnswerId) {
        int quIndex = 0;  // 问题索引
        Map<Integer,Question> questionMap = new ConcurrentHashMap<Integer,Question>();  // 问题Map
        for (Question question : questions) {
            new Thread(new RAnswerQuestionMap(quIndex++,questionMap,surveyAnswerId,question)).start();  // 创建并启动新线程
        }
        while (questionMap.size() != questions.size()){
            try {
                Thread.sleep(1);  // 等待1毫秒
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // 重新中断当前线程
                e.printStackTrace();

            }
        }
        return questionMap;  // 返回新的question Map
    }



    /**
     * RAnswerQuestionMap类用于回答问题并更新问题映射
     */
    public class RAnswerQuestionMap implements Runnable{
        private int quIndex;                // 问题索引
        private Map<Integer,Question> questionMap;    // 问题映射
        private String surveyAnswerId;         // 调查回答ID
        private Question question;             // 问题对象

        /**
         * 构造函数，初始化RAnswerQuestionMap对象
         * @param quIndex 问题索引
         * @param questionMap 问题映射
         * @param surveyAnswerId 调查回答ID
         * @param question 问题对象
         */
        public RAnswerQuestionMap(int quIndex, Map<Integer,Question> questionMap, String surveyAnswerId, Question question){
            this.quIndex = quIndex;
            this.questionMap = questionMap;
            this.surveyAnswerId = surveyAnswerId;
            this.question = question;
        }

        @Override
        /**
         * 运行方法
         */
        public void run() {
            // 获取SurveyAnswerManager实例
            SurveyAnswerManager surveyManager = SpringContextHolder.getBean(SurveyAnswerManagerImpl.class);
            // 获取指定surveyAnswerId的questionAnswer并存入question中
            surveyManager.getquestionAnswer(surveyAnswerId, question);
            // 将question存入questionMap中
            questionMap.put(quIndex, question);
        }

    }


}